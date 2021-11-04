package scc.application.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import scc.domain.entities.Channel;

@Repository
public interface ChannelsRepository extends CosmosRepository<Channel, String> {
}
