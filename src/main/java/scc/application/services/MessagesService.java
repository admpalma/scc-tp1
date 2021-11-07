package scc.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scc.application.exceptions.EntityNotFoundException;
import scc.application.exceptions.MessageNotFoundException;
import scc.application.exceptions.PermissionDeniedException;
import scc.application.repositories.ChannelsRepository;
import scc.application.repositories.MessagesRepository;
import scc.domain.entities.Message;

@Service
public class MessagesService {

    private final MessagesRepository messages;
    private final ChannelsRepository channels;

    @Autowired
    public MessagesService(MessagesRepository messages, ChannelsRepository channels) {
        this.messages = messages;
        this.channels = channels;
    }

    public Message addMessage(Message message, String principal) {
        if (!channels.findById(message.getChannel()).orElseThrow(EntityNotFoundException::new).getMembers().contains(principal)) {
            throw new PermissionDeniedException();
        }
        return messages.save(message);
    }

    public Message getMessage(String id, String principal) {
        Message message = messages.findById(id).orElseThrow(MessageNotFoundException::new);
        if (!channels.findById(message.getChannel()).orElseThrow(EntityNotFoundException::new).getMembers().contains(principal)) {
            throw new PermissionDeniedException();
        }
        return message;
    }

}
