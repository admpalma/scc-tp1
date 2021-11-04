package scc.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scc.application.services.UsersService;
import scc.domain.entities.User;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UsersService users;

    @Autowired
    public UsersController(UsersService users) {
        this.users = users;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Adding user: " + user);
        return users.addUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        log.info("Requesting user with id: " + id);
        return users.getUser(id).orElseThrow();
    }

    @GetMapping("/{id}/channels")
    public List<String> getUserChannels(@PathVariable String id) {
        log.info("Requesting user with id: " + id);
        return users.getUserChannels(id).orElseThrow();
    }

    @PostMapping("/{id}/subscribe/{channelId}")
    public void subscribeToChannel(@PathVariable String id, @PathVariable String channelId) {
        log.info("Subscribing to: " + channelId + " user with id: " + id);
        users.subscribeToChannel(id, channelId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        log.info("Deleting user with id: " + id);
        users.deleteUser(id);
    }

}

