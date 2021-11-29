package scc.application.repositories;

import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import scc.domain.entities.Channel;

@Repository
public interface ChannelsRepository extends CosmosRepository<Channel, String> {
    Mono<CosmosItemResponse<Channel>> addUserToChannel(String channelId, String userId);
}
