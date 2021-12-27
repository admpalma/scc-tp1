package scc.application.services;

import org.springframework.stereotype.Service;
import scc.application.exceptions.EntityNotFoundException;
import scc.application.exceptions.MessageNotFoundException;
import scc.application.exceptions.PermissionDeniedException;
import scc.application.repositories.ChannelsRepository;
import scc.application.repositories.MessagesRepository;
import scc.domain.entities.Message;
import scc.domain.entities.User;

import java.util.List;

@Service
public class MessagesService {

    private final MessagesRepository messages;
    private final ChannelsRepository channels;

    public MessagesService(MessagesRepository messages, ChannelsRepository channels) {
        this.messages = messages;
        this.channels = channels;
    }

    public Message addMessage(Message message, String principal) {
        List<User> members = channels.findById(message.getChannel()).orElseThrow(EntityNotFoundException::new).getMembers();
        if (members.stream().map(User::getId).noneMatch(s -> s.equals(principal))) {
            throw new PermissionDeniedException();
        }
        return messages.save(message);
    }

    public Message getMessage(String id, String principal) {
        Message message = messages.findById(id).orElseThrow(MessageNotFoundException::new);
        List<User> members = channels.findById(message.getChannel()).orElseThrow(EntityNotFoundException::new).getMembers();
        if (members.stream().map(User::getId).noneMatch(s -> s.equals(principal))) {
            throw new PermissionDeniedException();
        }
        return message;
    }

}
