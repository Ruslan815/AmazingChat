package ru.cft.team2.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.cft.team2.chat.model.Message;
import ru.cft.team2.chat.repository.MessageRepository;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void create(Message someMessage) {
        messageRepository.save(someMessage);
    }

    public List<Message> getAll() {
        return messageRepository.findAll(Sort.by(Sort.Direction.DESC, "time"));
    }
}
