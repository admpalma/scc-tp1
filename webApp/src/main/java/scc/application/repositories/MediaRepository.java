package scc.application.repositories;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MediaRepository {
    String uploadMedia(byte[] data);

    byte[] downloadMedia(String fileName);
}
