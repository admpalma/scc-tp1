package scc.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scc.application.repositories.UsersRepository;
import scc.domain.entities.User;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository users;

    @Autowired
    public UsersService(UsersRepository users) {
        this.users = users;
    }

    public User addUser(User user) {
        return users.save(user);
    }

    public Optional<User> getUser(String id, String principal) {
        return hasPermission(id, principal) ? users.findById(id) : Optional.empty();
    }

    public Optional<List<String>> getUserChannels(String id, String principal) {
        return hasPermission(id, principal) ? users.findById(id).map(User::getChannelIds) : Optional.empty();
    }

    public void subscribeToChannel(String id, String channelId, String principal) {
        if (!hasPermission(id, principal)) {
            return;
        }
        users.findById(id).ifPresent(user -> {
            user.getChannelIds().add(channelId);
            users.save(user);
        });
    }

    public void deleteUser(String id, String principal) {
        if (!hasPermission(id, principal)) {
            return;
        }
        users.deleteById(id);
    }

    private boolean hasPermission(String id, String principal) {
        return id.equals(principal);
    }
}
