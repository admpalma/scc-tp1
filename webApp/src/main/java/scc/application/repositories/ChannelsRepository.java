package scc.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import scc.domain.entities.Channel;

@NoRepositoryBean
public interface ChannelsRepository extends JpaRepository<Channel,String> {
    Channel addUserToChannel(String channelId, String userId);
}
