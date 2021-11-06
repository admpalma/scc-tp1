package scc.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import scc.application.services.UsersService;
import scc.domain.entities.User;
import scc.rest.models.AuthenticationModel;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AuthenticationManager authenticationManager;

    private final UsersService users;

    @Autowired
    public UsersController(AuthenticationManager authenticationManager, UsersService users) {
        this.authenticationManager = authenticationManager;
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
        log.info("Getting the channels of the user with id: " + id);
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

    @PostMapping("/auth")
    public ResponseEntity<Object> authenticateUser(@RequestBody AuthenticationModel authentication) {
        log.info("Authenticating: " + authentication);
        String username = authentication.getUser();
        String password = authentication.getPwd();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticated = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext()
                .setAuthentication(authenticated);
        return new ResponseEntity<>(authenticated.getPrincipal(), HttpStatus.OK);
    }

}

