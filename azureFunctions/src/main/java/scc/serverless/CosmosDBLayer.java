package scc.serverless;

import com.azure.cosmos.*;

public class CosmosDBLayer implements AutoCloseable {
    private static final String CONNECTION_URL = System.getenv("COSMOSDB_URL"); //https://scc212255797.documents.azure.com:443/"
    private static final String DB_KEY = System.getenv("COSMOSDB_KEY"); //"vyrrKFBXS2B9EYodV6WQjSVXlUHT8pLTKJrEmwyyrCSj5mWb5JoftBcxxpyqxl26d91mWNoSrmK05oUmCnUydg==";
    private static final String DB_NAME = System.getenv("COSMOSDB_DATABASE"); //"scc2122db55797";

    public CosmosDBLayer() {
        client = getCosmosClient();
    }

    private final CosmosClient client;

    private CosmosClient getCosmosClient() {
        return new CosmosClientBuilder()
                .endpoint(CONNECTION_URL)
                .key(DB_KEY)
                .directMode()
                .consistencyLevel(ConsistencyLevel.SESSION)
                .connectionSharingAcrossClientsEnabled(true)
                .contentResponseOnWriteEnabled(true)
                .buildClient();
    }

    public CosmosContainer getContainer(String containerName) {
        return client.getDatabase(DB_NAME).getContainer(containerName);
    }

    @Override
    public void close() {
        client.close();
    }
}
