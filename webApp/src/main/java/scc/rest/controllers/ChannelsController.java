package scc.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scc.application.exceptions.*;
import scc.application.services.ChannelsService;
import scc.domain.entities.Channel;
import scc.domain.entities.Message;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/channel")
public class ChannelsController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ChannelsService channels;

    @Autowired
    public ChannelsController(ChannelsService channels) {
        this.channels = channels;
    }

    @PostMapping
    public ResponseEntity<Channel> addChannel(@RequestBody Channel channel) {
        log.info("Adding channel: " + channel);
        try {
            return new ResponseEntity<>(channels.addChannel(channel), HttpStatus.OK);
        } catch (EntityAlreadyExistsException e) {
            log.info("Channel already exists.");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Channel> getChannel(@PathVariable String id) {
        log.info("Requesting channel with id: " + id);
        try {
            return new ResponseEntity<>(channels.getChannel(id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.info("Channel does not exist.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Adds a user to the members of the given private channel and
     * registers the channel in the subscribed channels of the user
     */
    @PostMapping("/{channelId}/add/{userId}")
    public ResponseEntity<String> addUserToChannel(@PathVariable String channelId, @PathVariable String userId, Principal principal) {
        log.info("Adding user to private channel");
        try {
            channels.addUserToChannel(channelId, userId, principal.getName());
            return new ResponseEntity<>("Subscription made successfully.", HttpStatus.OK);
        } catch (PermissionDeniedException e) {
            return new ResponseEntity<>("Permission denied.", HttpStatus.FORBIDDEN);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("The given channel does not exist.", HttpStatus.NOT_FOUND);
        } catch (PrivateChannelException e) {
            return new ResponseEntity<>("You cannot subscribe to a private channel.", HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping("/{channelId}/messages")
    public ResponseEntity<List<Message>> getChannelMessages(@PathVariable String channelId, @RequestParam int st, @RequestParam int len, Principal principal) {
        log.info("Get the list of messages of channel with id: " + channelId);
        try {
            return new ResponseEntity<>(channels.getMessages(channelId, st, len, principal.getName()), HttpStatus.OK);
        } catch (PermissionDeniedException e) {
            log.info("You must be subscribed to the channel to get its messages.");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (EntityNotFoundException e) {
            log.info("The given channel does not exist.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (PublicChannelException e) {
            log.info("The given channel is not private.");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NegativeInputsException e) {
            log.info("The given inputs must be positive.");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}

