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
import scc.application.repositories.ChannelsRepository;
import scc.domain.entities.Channel;

@Repository
public class CosmosChannelsRepository extends SimpleCosmosRepository<Channel, String> implements ChannelsRepository {

    private final CosmosAsyncContainer channels;

    @SuppressWarnings("unchecked")
    public CosmosChannelsRepository(CosmosOperations dbOperations, CosmosAsyncClient client, @Value("${azure.cosmos.database}") String databaseName) {
        super((CosmosEntityInformation<Channel, String>) CosmosEntityInformation.getInstance(Channel.class), dbOperations);
        String containerName = CosmosEntityInformation.getInstance(Channel.class).getContainerName();
        this.channels = client
                .getDatabase(databaseName)
                .getContainer(containerName);
    }

    @Override
    public Mono<CosmosItemResponse<Channel>> addUserToChannel(String channelId, String userId) {
        PartitionKey partitionKey = new PartitionKey(channelId);
        CosmosPatchOperations op = CosmosPatchOperations.create().add("/members/0", userId);
        return channels.patchItem(channelId, partitionKey, op, Channel.class);
    }
}
