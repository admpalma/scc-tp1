package scc.application.services;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosPatchOperations;
import com.azure.cosmos.models.PartitionKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import scc.application.exceptions.*;
import scc.application.repositories.ChannelsRepository;
import scc.application.repositories.MessagesRepository;
import scc.application.repositories.UsersRepository;
import scc.domain.entities.Channel;
import scc.domain.entities.Message;
import scc.domain.entities.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelsService {
    private static final String MEMBERS_PATH="/members/0";
    private final ChannelsRepository channels;
    //private final UsersRepository users;
    private final MessagesRepository messages;
    private final CosmosDatabase cosmosDatabase;

    @Value("${containers.channels.name}")
    private String channelContainerName;



    public Channel addChannel(Channel channel) {
        if (channel.getId() != null && channels.existsById(channel.getId())) {
            throw new EntityAlreadyExistsException();
        }
        return channels.save(channel);
    }

    public Channel getChannel(String id) {
        if (!channels.existsById(id)) {
            throw new EntityNotFoundException();
        }
        return channels.findById(id).orElseThrow();
    }
    private int addSubscribedUser(String channelId,String userId){
        CosmosContainer channels = cosmosDatabase.getContainer(channelContainerName);
        PartitionKey partitionKey = new PartitionKey(channelId);
        CosmosPatchOperations op = CosmosPatchOperations.create().add(
                MEMBERS_PATH, userId);
        CosmosItemResponse<User> res = null;
        res = channels.patchItem(channelId, partitionKey, op, User.class);
        return res.getStatusCode();
    }
    public void addUserToChannel(String channelId, String userId, String principal) {
        if (!hasPermission(userId, principal)) {
            throw new PermissionDeniedException();
        }
        Channel channel = channels.findById(channelId).orElseThrow(EntityNotFoundException::new);
        if (channel.isPublicChannel()) {
            throw new PrivateChannelException();
        }
        if(HttpStatus.OK.value() == addSubscribedUser(channelId,userId)){
            channels.save(channel);
        }

    }

    public List<Message> getMessages(String channelId, int st, int len, String principal) {
        if (st < 0 || len <= 0) {
            throw new NegativeInputsException();
        }
        Channel channel = channels.findById(channelId).orElseThrow(EntityNotFoundException::new);
        if (!channel.getMembers().contains(principal)) {
            throw new PermissionDeniedException();
        }
        return messages.findPageByChannel(channelId, st, len);
    }

    private boolean hasPermission(String id, String principal) {
        return id.equals(principal);
    }
}
