package scc.application.services;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.implementation.NotFoundException;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosPatchOperations;
import com.azure.cosmos.models.PartitionKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import scc.application.exceptions.EntityAlreadyExistsException;
import scc.application.exceptions.EntityNotFoundException;
import scc.application.exceptions.PermissionDeniedException;
import scc.application.exceptions.PrivateChannelException;
import scc.application.repositories.ChannelsRepository;
import scc.application.repositories.UsersRepository;
import scc.domain.entities.Channel;
import scc.domain.entities.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final String channelsPath = "/channelIds/0";
    @Value("$containers.users.name")
    private String usersContainerName; //"Users"
    private final UsersRepository users;
    private final ChannelsRepository channels;
    private final CosmosDatabase cosmosDatabase;

    public User addUser(User user) {
        if(users.existsById(user.getId())){
            throw new EntityAlreadyExistsException();
        }
        return users.save(user);
    }

    public User getUser(String id, String principal) {
        if (!hasPermission(id, principal)) {
            throw new PermissionDeniedException();
        }
        return users.findById(id).orElseThrow();
    }

    public Optional<List<String>> getUserChannels(String id, String principal) {
        if (!hasPermission(id, principal)) {
            throw new PermissionDeniedException();
        }
        return users.findById(id).map(User::getChannelIds);
    }
    private int addSubscribedChannel(String userId,String channelId){
        CosmosContainer users = cosmosDatabase.getContainer(usersContainerName);
        PartitionKey partitionKey = new PartitionKey(userId);

        CosmosPatchOperations op = CosmosPatchOperations.create().add(
                channelsPath, channelId);
        CosmosItemResponse<User> res = null;
        res = users.patchItem(userId, partitionKey, op, User.class);
        return res.getStatusCode();
    }
    public void subscribeToChannel(String id, String channelId, String principal) {
        if (!hasPermission(id, principal)) {
            throw new PermissionDeniedException();
        }
        Channel channel = channels.findById(channelId).orElseThrow(EntityNotFoundException::new);
        if (!channel.isPublicChannel()) {
            throw new PrivateChannelException();
        }
        /*
        users.findById(id).ifPresent(user -> {
            user.getChannelIds().add(channelId);
            users.save(user);
        }); */
        if(HttpStatus.OK.value()==addSubscribedChannel(id,channelId)){
            channel.getMembers().add(id);
            channels.save(channel);
        }
    }


    public void deleteUser(String id, String principal) {
        if (!hasPermission(id, principal)) {
            throw new PermissionDeniedException();
        }
        users.deleteById(id);
        //TODO delete all occurrences on channels' members
    }

    private boolean hasPermission(String id, String principal) {
        return id.equals(principal);
    }

}
