package scc.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scc.application.exceptions.*;
import scc.application.repositories.ChannelsRepository;
import scc.application.repositories.MessagesRepository;
import scc.application.repositories.UsersRepository;
import scc.domain.entities.Channel;
import scc.domain.entities.Message;
import scc.domain.entities.User;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelsService {

    private final ChannelsRepository channels;
    private final UsersRepository users;
    private final MessagesRepository messages;

    @Autowired
    public ChannelsService(ChannelsRepository channels, UsersRepository users, MessagesRepository messages) {
        this.channels = channels;
        this.users = users;
        this.messages = messages;
    }

    public Channel addChannel(Channel channel) {
        if (channels.existsById(channel.getId())) {
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
        if (!hasPermission(userId, principal)) {
            throw new PermissionDeniedException();
        }
        Channel channel = channels.findById(channelId).orElseThrow(EntityNotFoundException::new);
        if (channel.isPublicChannel()) {
            throw new PrivateChannelException();
        }
        User user = users.findById(userId).orElseThrow();
        user.getChannelIds().add(channelId);
        channel.getMembers().add(userId);
        users.save(user);
        channels.save(channel);
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
