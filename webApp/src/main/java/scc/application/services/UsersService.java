package scc.application.services;

import org.springframework.stereotype.Service;
import scc.application.exceptions.EntityAlreadyExistsException;
import scc.application.exceptions.EntityNotFoundException;
import scc.application.exceptions.PermissionDeniedException;
import scc.application.exceptions.PrivateChannelException;
import scc.application.repositories.ChannelsRepository;
import scc.application.repositories.UsersRepository;
import scc.domain.entities.Channel;
import scc.domain.entities.ChannelIdProjection;
import scc.domain.entities.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository users;
    private final ChannelsRepository channels;

    public UsersService(UsersRepository users, ChannelsRepository channels) {
        this.users = users;
        this.channels = channels;
    }

    public User addUser(User user) {
        if (users.existsById(user.getId())) {
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
    public List<String> getUserChannels(String id, String principal) {
        if (!hasPermission(id, principal)) {
            throw new PermissionDeniedException();
        }
        ChannelIdProjection channelIdProjection = users.findChannelidsById(id);
        List<String> channelIds = new LinkedList<>();
        channelIdProjection.getChannelids().forEach(channelSummary -> channelIds.add(channelSummary.getId()));
        return channelIds;
    }

    public void subscribeToChannel(String userId, String channelId, String principal) {
        if (!hasPermission(userId, principal)) {
            throw new PermissionDeniedException();
        }
        Channel channel = channels.findById(channelId).orElseThrow(EntityNotFoundException::new);
        if (!channel.isPublicChannel()) {
            throw new PrivateChannelException();
        }
        User user = users.findById(userId).get();
        user.getChannelids().add(channel);
        users.save(user);
       }

    public void deleteUser(String id, String principal) {
        if (!hasPermission(id, principal)) {
            throw new PermissionDeniedException();
        }
    }

    private boolean hasPermission(String id, String principal) {
        return id.equals(principal);
    }

}
