package scc.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scc.application.services.ChannelsService;
import scc.domain.entities.Channel;
import scc.domain.entities.Message;

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
    public Channel addChannel(@RequestBody Channel channel) {
        log.info("Adding channel: " + channel);
        return channels.addChannel(channel);
    }

    @GetMapping("/{id}")
    public Channel getChannel(@PathVariable String id) {
        log.info("Requesting channel with id: " + id);
        return channels.getChannel(id).orElseThrow();
    }

    @GetMapping("/{channelId}/add/{userId}")
    public void getUserChannels(@PathVariable String channelId, @PathVariable String userId) {
        log.info("Adding user with id: " + userId + " to the members of private channel with id " + channelId +
                " and registers the channel in the subscribed channels of the user");
        channels.addUserToChannel(channelId, userId);
    }

    @GetMapping("/{channelId}/messages")
    public List<Message> getMessages(@PathVariable String channelId, @RequestParam int st, @RequestParam int len) {
        log.info("Get the list of messages of channel with id: " + channelId);
        return channels.getMessages(channelId, st, len);

    }


}

