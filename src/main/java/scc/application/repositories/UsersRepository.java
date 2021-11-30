package scc.application.repositories;

import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Mono;
import scc.domain.entities.User;

@NoRepositoryBean
public interface UsersRepository extends CosmosRepository<User, String> {
    Mono<CosmosItemResponse<User>> subscribeToChannel(String userId, String channelId);
}
