package ru.cft.team2.chat.service;

import org.springframework.stereotype.Service;
import ru.cft.team2.chat.model.Chat;
import ru.cft.team2.chat.model.ChatView;
import ru.cft.team2.chat.repository.ChatRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public ChatView create(Chat someChat) throws Exception {
        if (someChat.getName() == null || someChat.getName().equals("")) {
            throw new Exception();
        }
        return new ChatView(chatRepository.save(someChat));
    }

    public List<ChatView> getAll() {
        List<Chat> tempList = chatRepository.findAll();
        List<ChatView> responseList = new ArrayList<>();
        for (Chat tempChat: tempList) {
            responseList.add(new ChatView(tempChat));
        }
        return responseList;
    }

    @Transactional
    public boolean enterChat(Integer userId, Integer chatId) {
        Chat chat = chatRepository.getById(chatId);
        boolean isUserAlreadyInChat = chat.getChatMembers().add(userId);
        if (!isUserAlreadyInChat) {
            chatRepository.save(chat);
        }
        return isUserAlreadyInChat;
    }

    @Transactional
    public boolean leaveChat(Integer userId, Integer chatId) {
        Chat chat = chatRepository.getById(chatId);
        boolean isUserAlreadyInChat = chat.getChatMembers().remove(chatId);
        if (isUserAlreadyInChat) {
            chatRepository.save(chat);
        }
        return isUserAlreadyInChat;
    }

    public boolean isUserInChat(Integer userId, Integer chatId) {
        Chat chat = chatRepository.getById(chatId);
        return chat.getChatMembers().contains(userId);
    }

    public boolean isChatExist (Integer chatId) {
        if (chatId == null) return false;
        return chatRepository.existsById(chatId);
    }
}
