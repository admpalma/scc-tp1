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

    public Optional<User> getUser(String id) {
        return users.findById(id);
    }

    public Optional<List<String>> getUserChannels(String id) {
        return users.findById(id).map(User::getChannelIds);
    }

    public void subscribeToChannel(String id, String channelId) {
        users.findById(id).ifPresent(user -> {
            user.getChannelIds().add(channelId);
            users.save(user);
        });
    }

    public void deleteUser(String id) {
        users.deleteById(id);
    }
}
