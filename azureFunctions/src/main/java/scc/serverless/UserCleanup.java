package scc.serverless;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import org.slf4j.LoggerFactory;
import scc.entities.Channel;
import scc.entities.Message;
import scc.entities.RemovedUser;
import scc.entities.UserEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class UserCleanup {

    private static final String CRON_EXP = "0 5 * * * *";
    private static final String USERS_CONTAINER_NAME = "Users";
    private static final String MESSAGES_CONTAINER_NAME = "Messages";
    private static final String CHANNELS_CONTAINER_NAME = "Channels";
    private static final String REMOVED_USERS_CONTAINER_NAME = "RemovedUsers";
    //final static Logger log = LoggerFactory.getLogger(UserCleanup.class);

    @FunctionName("periodic-cleanup")
    public void cosmosFunction(@TimerTrigger(name = "periodicCleanup", schedule = CRON_EXP)
                                       String timerInfo, ExecutionContext context) {
        Logger log = context.getLogger();
        log.info("TIMER INFO IS " + timerInfo);

        try (CosmosDBLayer cosmosDBLayer2 = new CosmosDBLayer()) {
            CosmosContainer removedUsers = cosmosDBLayer2.getContainer(REMOVED_USERS_CONTAINER_NAME);
            CosmosPagedIterable<RemovedUser> removedUsers1 = removedUsers.queryItems("SELECT * FROM RemovedUsers", new CosmosQueryRequestOptions(), RemovedUser.class);
            removedUsers1.forEach(removedUser -> {
                try {
                    cleanUp(cosmosDBLayer2, removedUser.getId(), log);
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
                try {
                    removedUsers.deleteItem(removedUser, new CosmosItemRequestOptions());
                } catch (Exception e2) {
                    log.info(e2.getMessage());
                }
            });
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public static void cleanUp(CosmosDBLayer cosmosDBLayer2, String userId, Logger log) {
        CosmosContainer messages = cosmosDBLayer2.getContainer(MESSAGES_CONTAINER_NAME);
        CosmosContainer users = cosmosDBLayer2.getContainer(USERS_CONTAINER_NAME);
        CosmosContainer channels = cosmosDBLayer2.getContainer(CHANNELS_CONTAINER_NAME);
        updateUserMessages(messages, userId, log);
        updateUserChannels(users, channels, messages, userId, log);
        users.deleteItem(userId, new PartitionKey(userId), new CosmosItemRequestOptions());
    }

    public static void deleteMessagesByChannel(CosmosContainer messages, String channelId, Logger log) {
        try {
            CosmosPagedIterable<Message> messageList = messages.queryItems("SELECT * FROM Messages WHERE Messages.channel=\"" + channelId + "\"", new CosmosQueryRequestOptions(), Message.class);
            messageList.forEach(message -> messages.deleteItem(message, new CosmosItemRequestOptions()));
        } catch (Exception e2) {
            log.info(e2.getMessage());
        }
    }

    public static void updateUserMessages(CosmosContainer messages, String userId, Logger log) {
        CosmosPagedIterable<Message> messageList = messages.queryItems("SELECT * FROM Messages WHERE Messages.user=\"" + userId + "\"", new CosmosQueryRequestOptions(), Message.class);
        messageList.forEach(m -> {
            m.setUser("USER REMOVED!");
            messages.replaceItem(m, m.getId(), new PartitionKey(m.getChannel()), new CosmosItemRequestOptions());
        });
    }

    public static void updateUserChannels(CosmosContainer users, CosmosContainer channels, CosmosContainer messages, String userId, Logger log) {
        CosmosPagedIterable<UserEntity> userEntities = users.queryItems("SELECT * FROM Users WHERE Users.id=\"" + userId + "\"", new CosmosQueryRequestOptions(), UserEntity.class);
        UserEntity user = userEntities.stream().findFirst().get();
        List<String> channelsList = user.getChannelIds();
        channelsList.forEach(channelId -> removeUserFromChannelMembers(channels, messages, users, channelId, userId, log));
    }

    public static void removeChannelIdFromUsersChannels(CosmosContainer users, Channel channel, Logger log) {
        channel.getMembers().forEach(memberId -> {
            try {
                if (!channel.getOwner().equals(memberId)) {
                    CosmosPagedIterable<UserEntity> userEntities = users.queryItems("SELECT * FROM Users WHERE Users.id=\"" + memberId + "\"", new CosmosQueryRequestOptions(), UserEntity.class);
                    UserEntity user = userEntities.stream().findFirst().orElseThrow(() -> new NoSuchElementException("USER WITH ID " + memberId + " NOT FOUND"));
                    int index = user.getChannelIds().indexOf(channel.getId());
                    PartitionKey partitionKey = new PartitionKey(memberId);
                    CosmosPatchOperations op = CosmosPatchOperations.create().remove("/channelIds/" + index);
                    users.patchItem(memberId, partitionKey, op, UserEntity.class).getStatusCode();
                    log.info("CHANNEL REMOVED SUCCESSFULLY!");
                }
            } catch (Exception e) {
                log.info(e.getMessage());
                log.info(String.format("FAILED TO REMOVE CHANNEL %s from USER %s list of channels", channel.getId(), memberId));
            }
        });
    }

    public static void removeUserFromChannelMembers(CosmosContainer channels, CosmosContainer messages, CosmosContainer users, String channelId, String userId, Logger log) {
        try {
            CosmosPagedIterable<Channel> channelCosmosPagedIterable = channels.queryItems("SELECT * FROM Channels WHERE Channels.id='" + channelId + "'", new CosmosQueryRequestOptions(), Channel.class);
            Channel channel = channelCosmosPagedIterable.stream().findFirst().orElseThrow(() -> new NoSuchElementException("CHANNEL " + channelId + " NOT FOUND!"));
            if (userId.equals(channel.getOwner())) {
                log.info(String.format("GOING TO REMOVE CHANNEL %s FROM USER %s", channelId, userId));
                removeChannelIdFromUsersChannels(users, channel, log);
                channels.deleteItem(channel, new CosmosItemRequestOptions());
                deleteMessagesByChannel(messages, channelId, log);
            }
            int index = channel.getMembers().indexOf(userId);
            PartitionKey partitionKey = new PartitionKey(channelId);
            CosmosPatchOperations op = CosmosPatchOperations.create().remove("/members/" + index);
            channels.patchItem(channelId, partitionKey, op, Channel.class);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
