package scc.application.services;

import org.springframework.stereotype.Service;
import scc.application.exceptions.EntityNotFoundException;
import scc.application.exceptions.MessageNotFoundException;
import scc.application.exceptions.PermissionDeniedException;
import scc.application.repositories.ChannelsRepository;
import scc.application.repositories.MessagesRepository;
import scc.application.repositories.SearchMessages;
import scc.domain.entities.Message;

import java.util.List;

@Service
public class MessagesService {

    private final MessagesRepository messages;
    private final ChannelsRepository channels;
    private final SearchMessages searchMessages;

    public MessagesService(MessagesRepository messages, ChannelsRepository channels, SearchMessages searchMessages) {
        this.messages = messages;
        this.channels = channels;
        this.searchMessages = searchMessages;
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

    public List<String> getMessagesWithText(String queryText) {
        return searchMessages.getMessagesWithText(queryText);
    }

}
