package scc.infrastructure.repositories;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosPatchOperations;
import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.core.CosmosOperations;
import com.azure.spring.data.cosmos.repository.support.CosmosEntityInformation;
import com.azure.spring.data.cosmos.repository.support.SimpleCosmosRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import scc.application.repositories.UsersRepository;
import scc.domain.entities.User;

@Repository
public class CosmosUsersRepository extends SimpleCosmosRepository<User, String> implements UsersRepository {

    private final CosmosAsyncContainer users;

    @SuppressWarnings("unchecked")
    public CosmosUsersRepository(CosmosOperations dbOperations, CosmosAsyncClient client, @Value("${azure.cosmos.database}") String databaseName) {
        super((CosmosEntityInformation<User, String>) CosmosEntityInformation.getInstance(User.class), dbOperations);
        String containerName = CosmosEntityInformation.getInstance(User.class).getContainerName();
        this.users = client
                .getDatabase(databaseName)
                .getContainer(containerName);
    }

    @Override
    public Mono<CosmosItemResponse<User>> subscribeToChannel(String channelId, String userId) {
        PartitionKey partitionKey = new PartitionKey(userId);
        CosmosPatchOperations op = CosmosPatchOperations.create().add("/channelIds/0", channelId);
        return users.patchItem(userId, partitionKey, op, User.class);
    }
}
