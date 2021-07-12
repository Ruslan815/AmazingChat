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

    public List<MessageView> getAllByChatId(Integer chatId) {
        List<Message> tempList = messageRepository.findAllByChatId(chatId, Sort.by(Sort.Direction.DESC, "sendTime"));
        List<MessageView> responseList = new ArrayList<>();
        List<Integer> deletedMessagesIdsList = new ArrayList<>();
        long currentTimeSec = System.currentTimeMillis() / 1000;
        for (Message tempMessage : tempList) {
            long currentMessageLifeTime = currentTimeSec - tempMessage.getSendTimeSec(); // sendTimeSec is always not null
            Long messageLifeTimeSec = tempMessage.getLifetimeSec();
            if (messageLifeTimeSec == null || currentMessageLifeTime < messageLifeTimeSec) { // If lifeTimeSec not specified
                responseList.add(new MessageView(tempMessage));
            } else {
                deletedMessagesIdsList.add(tempMessage.getMessageId());
            }
        }
        if (!deletedMessagesIdsList.isEmpty()) {
            messageRepository.deleteAllById(deletedMessagesIdsList);
        }
        return responseList;
    }
}
