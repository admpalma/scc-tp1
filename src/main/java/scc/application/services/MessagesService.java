package scc.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scc.application.repositories.MessagesRepository;
import scc.domain.entities.Message;

import java.util.Optional;

@Service
public class MessagesService {

    private final MessagesRepository messages;

    @Autowired
    public MessagesService(MessagesRepository messages) {
        this.messages = messages;
    }

    public Message addMessage(Message message) {
        return messages.save(message);
    }

    public Optional<Message> getMessage(String id) {
        return messages.findById(id);
    }

}
