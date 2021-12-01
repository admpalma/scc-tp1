package scc.serverless;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

import java.util.Objects;

public class AzureBlobReplicator {

    private final BlobContainerClient containerClient;

    public AzureBlobReplicator(String blobStoreConnection) {
        containerClient = new BlobServiceClientBuilder()
                .connectionString(Objects.requireNonNull(System.getenv(blobStoreConnection)))
                .buildClient()
                .getBlobContainerClient("images");
    }

    public void uploadMedia(String fileName, byte[] data) {
        containerClient.getBlobClient(fileName).upload(BinaryData.fromBytes(data));
    }

}
