package scc.rest.config;

import com.azure.cosmos.ConnectionMode;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.autoconfigure.cosmos.CosmosProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class CosmosDBConfig implements BeanPostProcessor {
    @Bean
    @Primary
    public CosmosClientBuilder cosmosClientBuilderWithPreferredRegion(CosmosProperties properties,
                                                   @Value("${cosmos.preferredRegion}") String preferredRegion) {
        CosmosClientBuilder cosmosClientBuilder = new CosmosClientBuilder();
        cosmosClientBuilder.key(properties.getKey())
                .consistencyLevel(properties.getConsistencyLevel())
                .endpoint(properties.getUri())
                .preferredRegions(List.of(preferredRegion));
        if (ConnectionMode.GATEWAY == properties.getConnectionMode()) {
            cosmosClientBuilder.gatewayMode();
        }
        return cosmosClientBuilder;
    }
}
