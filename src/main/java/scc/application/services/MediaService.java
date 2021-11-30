package scc.application.services;

import org.springframework.stereotype.Service;
import scc.application.repositories.MediaRepository;

@Service
public class MediaService {

    private final MediaRepository media;

    public MediaService(MediaRepository media) {
        this.media = media;
    }

    public String uploadMedia(byte[] data) {
        return media.uploadMedia(data);
    }

    public byte[] downloadMedia(String fileName) {
        return media.downloadMedia(fileName);
    }
}
