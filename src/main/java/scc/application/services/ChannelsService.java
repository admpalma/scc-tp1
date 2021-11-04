package scc.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scc.application.repositories.ChannelsRepository;
import scc.application.repositories.MessagesRepository;
import scc.application.repositories.UsersRepository;
import scc.domain.entities.Channel;
import scc.domain.entities.Message;

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
        return channels.save(channel);
    }

    public Optional<Channel> getChannel(String id) {
        return channels.findById(id);
    }

    public void addUserToChannel(String channelId, String userId) {
        channels.findById(channelId).ifPresent(channel -> users.findById(userId).ifPresent(user -> {
            user.getChannelIds().add(channelId);
            channel.getMembers().add(userId);
            users.save(user);
            channels.save(channel);
        }));
    }

    public List<Message> getMessages(String channelId, int st, int len) {
        return messages.findPageByChannel(channelId, st, len);
    }
}
