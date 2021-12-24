package scc.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import scc.domain.entities.Channel;
import scc.domain.entities.ChannelIdProjection;
import scc.domain.entities.User;

@NoRepositoryBean
public interface UsersRepository extends JpaRepository<User,String> {
    Channel subscribeToChannel(String userId, String channelId);
    ChannelIdProjection findChannelidsById(String id);


}
