package scc.infrastructure.repositories;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.stereotype.Repository;
import scc.application.repositories.MediaRepository;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

@Repository
public class BlobMediaRepository implements MediaRepository {

    private final BlobContainerClient containerClient;

    public BlobMediaRepository() {
        /**
        containerClient = new BlobServiceClientBuilder()
                .connectionString(Objects.requireNonNull(System.getenv("BlobStoreConnection")))
                .buildClient()
                .getBlobContainerClient("images"); **/
        containerClient = null;

    }

    @Override
    public String uploadMedia(byte[] data) {
        String fileName = UUID.randomUUID() + ".jpg"; //TODO jpg to ease debug
        return fileName;
    }

    @Override
    public byte[] downloadMedia(String fileName) {
        return new byte[10];
    }
}
