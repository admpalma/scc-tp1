package scc.application.services;

import com.azure.cosmos.models.CosmosItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import scc.application.exceptions.*;
import scc.application.repositories.ChannelsRepository;
import scc.application.repositories.MessagesRepository;
import scc.application.repositories.UsersRepository;
import scc.domain.entities.Channel;
import scc.domain.entities.Message;
import scc.domain.entities.User;

import java.util.List;

@Service
public class ChannelsService {

    private final UsersRepository users;
    private final ChannelsRepository channels;
    private final MessagesRepository messages;

    public ChannelsService(UsersRepository users, ChannelsRepository channels, MessagesRepository messages) {
        this.users = users;
        this.channels = channels;
        this.messages = messages;
    }

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

    public void addUserToChannel(String channelId, String userId, String principal) {
        Channel channel = channels.findById(channelId).orElseThrow(EntityNotFoundException::new);
        if (channel.isPublicChannel() || !hasPermission(principal, channel.getOwner())) {
            throw new PrivateChannelException();
        }
        Mono<CosmosItemResponse<Channel>> channelMono = channels.addUserToChannel(channelId, userId);
        CosmosItemResponse<User> userResponse = users.subscribeToChannel(userId, channelId).blockOptional().orElseThrow();
        CosmosItemResponse<Channel> channelResponse = channelMono.blockOptional().orElseThrow();
        Assert.isTrue(userResponse.getStatusCode() == HttpStatus.OK.value(), "Could not add Channel as subscription");
        Assert.isTrue(channelResponse.getStatusCode() == HttpStatus.OK.value(), "Could not add User as member");
    }

    public List<Message> getMessages(String channelId, int st, int len, String principal) { //TODO coerce st len
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
