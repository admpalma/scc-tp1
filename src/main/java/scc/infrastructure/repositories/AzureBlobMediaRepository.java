package scc.infrastructure.repositories;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.stereotype.Repository;
import scc.application.repositories.MediaRepository;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Repository
public class AzureBlobMediaRepository implements MediaRepository {

    private final BlobContainerClient containerClient;

    public AzureBlobMediaRepository() {
        containerClient = new BlobServiceClientBuilder()
                .connectionString(Objects.requireNonNull(System.getenv("BlobStoreConnection")))
                .buildClient()
                .getBlobContainerClient("images");
    }

    @Override
    public String uploadMedia(byte[] data) {
        //Arrays.hashCode(data)
        String fileName = UUID.randomUUID()+ ".jpg"; //TODO jpg to ease debug
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(BinaryData.fromBytes(data));
        return fileName;
    }

    @Override
    public byte[] downloadMedia(String fileName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        containerClient.getBlobClient(fileName).downloadStream(outputStream);
        return outputStream.toByteArray();
    }
}
