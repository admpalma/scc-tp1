package scc.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scc.domain.entities.ChannelIdProjection;
import scc.domain.entities.User;

@Repository
public interface UsersChannelsProjectionRepository extends JpaRepository<User,String> {
    ChannelIdProjection findChannelidsById(String id);

}
