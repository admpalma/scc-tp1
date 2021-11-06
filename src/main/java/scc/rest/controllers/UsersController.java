package scc.rest.controllers;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
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
import scc.application.exceptions.EntityAlreadyExistsException;
import scc.application.exceptions.EntityNotFoundException;
import scc.application.exceptions.PermissionDeniedException;
import scc.application.exceptions.PrivateChannelException;
import scc.application.services.UsersService;
import scc.domain.entities.User;
import scc.rest.models.AuthenticationModel;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<User> addUser(@RequestBody User user) {
        log.info("Adding user: " + user);
        try {
            return new ResponseEntity<>(users.addUser(user), HttpStatus.OK);
        } catch (EntityAlreadyExistsException e) {
            log.info("User already exists.");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id, Principal principal) {
        log.info("Requesting user with id: " + id);
        try {
            return new ResponseEntity<>(users.getUser(id, principal.getName()), HttpStatus.OK);
        } catch (PermissionDeniedException e) {
            log.info("Permission denied.");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}/channels")
    public ResponseEntity<List<String>> getUserChannels(@PathVariable String id, Principal principal) {
        log.info("Getting the channels of the user with id: " + id);
        try {
            Optional<List<String>> userChannels = users.getUserChannels(id, principal.getName());
            if (userChannels.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(userChannels.get(), HttpStatus.OK);
        } catch (PermissionDeniedException e) {
            log.info("Permission denied.");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/{id}/subscribe/{channelId}")
    public ResponseEntity<String> subscribeToChannel(@PathVariable String id, @PathVariable String channelId, Principal principal) {
        log.info("Subscribing to: " + channelId + " user with id: " + id);
        try {
            users.subscribeToChannel(id, channelId, principal.getName());
            return new ResponseEntity<>("Subscription made successfully.", HttpStatus.OK);
        } catch (PermissionDeniedException e) {
            return new ResponseEntity<>("Permission denied.", HttpStatus.FORBIDDEN);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("The given channel does not exist.", HttpStatus.NOT_FOUND);
        } catch (PrivateChannelException e) {
            return new ResponseEntity<>("You cannot subscribe to a private channel.", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id, Principal principal) {
        log.info("Deleting user with id: " + id);
        try {
            users.deleteUser(id, principal.getName());
            return new ResponseEntity<>("User deleted.", HttpStatus.OK);
        } catch (PermissionDeniedException e) {
            return new ResponseEntity<>("Permission denied.", HttpStatus.FORBIDDEN);
        }
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

