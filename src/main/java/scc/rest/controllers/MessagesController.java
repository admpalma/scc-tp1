package scc.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scc.application.exceptions.EntityNotFoundException;
import scc.application.exceptions.MessageNotFoundException;
import scc.application.exceptions.PermissionDeniedException;
import scc.application.services.MessagesService;
import scc.domain.entities.Message;

import java.security.Principal;

@RestController
@RequestMapping("/messages")
public class MessagesController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MessagesService messages;

    @Autowired
    public MessagesController(MessagesService messages) {
        this.messages = messages;
    }

    @PostMapping
    public ResponseEntity<Message> addMessage(@RequestBody Message message, Principal principal) {
        log.info("Adding message: " + message);
        try {
            return new ResponseEntity<>(messages.addMessage(message, principal.getName()), HttpStatus.OK);
        } catch (PermissionDeniedException e) {
            log.info("You must be subscribed to the channel to post messages.");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (EntityNotFoundException e) {
            log.info("The message's channel does not exist.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable String id, Principal principal) {
        log.info("Getting message with id: " + id);
        try {
            return new ResponseEntity<>(messages.getMessage(id, principal.getName()), HttpStatus.OK);
        } catch (PermissionDeniedException e) {
            log.info("You must be subscribed to the channel to post messages.");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (EntityNotFoundException e) {
            log.info("The message's channel does not exist.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (MessageNotFoundException e) {
            log.info("The message does not exist.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
