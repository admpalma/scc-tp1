package scc.application.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import scc.application.exceptions.*;
import scc.application.repositories.ChannelsRepository;
import scc.application.repositories.MessagesRepository;
import scc.application.repositories.UsersRepository;
import scc.domain.entities.Channel;
import scc.domain.entities.Message;
import scc.domain.entities.User;
import scc.domain.entities.UserIdProjection;

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
        User user = users.findById(userId).orElseThrow(EntityNotFoundException::new);
        if (channel.isPublicChannel() || !hasPermission(principal, channel.getOwner())) {
            throw new PrivateChannelException();
        }
        user.getChannelIds().add(channel);
        users.save(user);
    }

    public List<Message> getMessages(String channelId, int st, int len, String principal) { //TODO coerce st len
        if (st < 0 || len <= 0) {
            throw new NegativeInputsException();
        }
        UserIdProjection userIdProjection = channels.findMemberidsById(channelId);
        if (userIdProjection.getMembers().stream().map(UserIdProjection.UserSummary::getId).noneMatch(userId -> userId.equals(principal))) {
            throw new PermissionDeniedException();
        }
        Pageable pageable = PageRequest.of(st,len);
        return messages.findByChannel(channelId, pageable);
    }

    private boolean hasPermission(String id, String principal) {
        return id.equals(principal);
    }
}
