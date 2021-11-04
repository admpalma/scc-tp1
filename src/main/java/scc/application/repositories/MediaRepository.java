package scc.application.repositories;

public interface MediaRepository {
    String uploadMedia(byte[] data);

    byte[] downloadMedia(String fileName);
}
