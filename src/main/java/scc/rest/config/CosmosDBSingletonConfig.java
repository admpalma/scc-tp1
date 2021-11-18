package scc.rest.config;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class CosmosDBSingletonConfig {
    @Value("${azure.cosmos.uri}")
    String con_url;
    @Value("${azure.cosmos.key}")
    String database_key;
    @Value("${azure.cosmos.database}")
    String database_name;
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public CosmosDatabase getCosmosDBLayer(){
        CosmosClient client = new CosmosClientBuilder()
                .endpoint(con_url)
                .key(database_key)
                .gatewayMode()		// replace by .directMode() for better performance
                .consistencyLevel(ConsistencyLevel.SESSION)
                .connectionSharingAcrossClientsEnabled(true)
                .contentResponseOnWriteEnabled(true)
                .buildClient();
        return client.getDatabase(database_name);
    }
}
