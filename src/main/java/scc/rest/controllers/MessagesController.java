package scc.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scc.application.services.MessagesService;
import scc.domain.entities.Message;

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
    public Message addMessage(@RequestBody Message message) {
        log.info("Adding message: " + message);
        return messages.addMessage(message);
    }

    @GetMapping("/{id}")
    public Message getMessage(@PathVariable String id) {
        log.info("Getting message with id: " + id);
        return messages.getMessage(id).orElseThrow();
    }

}
