package scc.application.services;

import com.azure.cosmos.models.CosmosItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
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

    private final UsersRepository users;
    private final ChannelsRepository channels;

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

    public void subscribeToChannel(String id, String channelId, String principal) {
        if (!hasPermission(id, principal)) {
            throw new PermissionDeniedException();
        }
        Channel channel = channels.findById(channelId).orElseThrow(EntityNotFoundException::new);
        if (!channel.isPublicChannel()) {
            throw new PrivateChannelException();
        }
        Mono<CosmosItemResponse<Channel>> channelMono = channels.addUserToChannel(channelId, id);
        CosmosItemResponse<User> userResponse = users.subscribeToChannel(channelId, id).blockOptional().orElseThrow();
        CosmosItemResponse<Channel> channelResponse = channelMono.blockOptional().orElseThrow();
        Assert.isTrue(userResponse.getStatusCode() == HttpStatus.OK.value(), "Could not add Channel as subscription");
        Assert.isTrue(channelResponse.getStatusCode() == HttpStatus.OK.value(), "Could not add User as member");
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
