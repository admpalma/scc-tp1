package scc.application.services;

import com.azure.cosmos.implementation.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UsersService {

    private final UsersRepository users;
    private final ChannelsRepository channels;

    @Autowired
    public UsersService(UsersRepository users, ChannelsRepository channels) {
        this.users = users;
        this.channels = channels;
    }

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

        users.findById(id).ifPresent(user -> {
            user.getChannelIds().add(channelId);
            users.save(user);
        });

        channel.getMembers().add(id);
        channels.save(channel);
    }


    public void deleteUser(String id, String principal) {
        if (!hasPermission(id, principal)) {
            throw new PermissionDeniedException();
        }
        users.deleteById(id);
    }

    private boolean hasPermission(String id, String principal) {
        return id.equals(principal);
    }

}
