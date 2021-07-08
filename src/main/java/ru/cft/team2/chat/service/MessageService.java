package ru.cft.team2.chat.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.cft.team2.chat.model.Message;
import ru.cft.team2.chat.model.MessageView;
import ru.cft.team2.chat.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public MessageView create(Message someMessage) {
        return new MessageView(messageRepository.save(someMessage));
    }

    public List<MessageView> getAll() {
        List<Message> tempList = messageRepository.findAll(Sort.by(Sort.Direction.DESC, "time"));
        List<MessageView> responseList = new ArrayList<>();
        for (Message tempMessage: tempList) {
            responseList.add(new MessageView(tempMessage));
        }
        return responseList;
    }
}
