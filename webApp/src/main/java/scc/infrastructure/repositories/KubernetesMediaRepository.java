package scc.infrastructure.repositories;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import scc.application.repositories.MediaRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Repository
@NoArgsConstructor
public class KubernetesMediaRepository implements MediaRepository {

    @Override
    public String uploadMedia(byte[] data) {
        try {
            String fileName = UUID.randomUUID() + ".jpg"; //TODO jpg to ease debug
            Path file = Path.of("/mnt/azure/", fileName);
            Files.write(file, data);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] downloadMedia(String fileName) {
        try {
            Path file = Path.of("/mnt/azure/", fileName);
            return Files.readAllBytes(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
