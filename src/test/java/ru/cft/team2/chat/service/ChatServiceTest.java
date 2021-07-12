package ru.cft.team2.chat.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.cft.team2.chat.model.Chat;
import ru.cft.team2.chat.model.ChatView;
import ru.cft.team2.chat.model.User;
import ru.cft.team2.chat.repository.ChatRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @MockBean
    private ChatRepository chatRepository;

    private final Integer userId = 1;
    private final String firstName = "cat";
    private final String lastName = "dog";
    private final Integer chatId = 1;
    private final String name = "someName";

    @Test
    void createSuccessfulChat() {
        Chat passedChat = new Chat(chatId, name);
        ChatView expectedChatView = new ChatView(passedChat);
        Mockito.when(chatRepository.save(passedChat)).thenReturn(passedChat);

        ChatView actualChatView = null;
        try {
            actualChatView = chatService.create(passedChat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedChatView, actualChatView);
    }

    @Test
    void createFailedNameIsNull() {
        Chat passedChat = new Chat(chatId, null);

        Throwable thrown = assertThrows(Exception.class, () -> {
            chatService.create(passedChat);
        });

        assertThat(thrown).isInstanceOf(Exception.class);
    }

    @Test
    void createFailedNameIsEmpty() {
        Chat passedChat = new Chat(chatId, "");

        Throwable thrown = assertThrows(Exception.class, () -> {
            chatService.create(passedChat);
        });

        assertThat(thrown).isInstanceOf(Exception.class);
    }

    @Test
    void getAllSuccessful() {
        List<Chat> passedList = new ArrayList<>();
        passedList.add(new Chat(1, "nameOne"));
        passedList.add(new Chat(2, "nameTwo"));
        passedList.add(new Chat(3, "nameThree"));

        List<ChatView> expectedList = new ArrayList<>();
        expectedList.add(new ChatView(1, "nameOne"));
        expectedList.add(new ChatView(2, "nameTwo"));
        expectedList.add(new ChatView(3, "nameThree"));

        Mockito.when(chatRepository.findAll()).thenReturn(passedList);
        List<ChatView> actualList = chatService.getAll();

        assertEquals(expectedList, actualList);
    }

    @Test
    void enterChatSuccessful() {
        User passedUser = new User(userId, firstName, lastName);
        Chat returnedChat = new Chat(chatId, name);
        returnedChat.setChatMembers(new HashSet<>());
        Mockito.when(chatRepository.getById(chatId)).thenReturn(returnedChat);
        Mockito.when(chatRepository.save(returnedChat)).thenReturn(null);

        boolean actualResult = chatService.enterChat(passedUser, chatId);

        assertTrue(actualResult);
    }

    @Test
    void enterChatFailedUserAlreadyInChat() {
        User passedUser = new User(userId, firstName, lastName);
        Chat returnedChat = new Chat(chatId, name);
        Set<User> users = new HashSet<>();
        users.add(passedUser);
        returnedChat.setChatMembers(users);
        Mockito.when(chatRepository.getById(chatId)).thenReturn(returnedChat);

        boolean actualResult = chatService.enterChat(passedUser, chatId);

        assertFalse(actualResult);
    }

    @Test
    void leaveChatSuccessful() {
        User passedUser = new User(userId, firstName, lastName);
        Chat returnedChat = new Chat(chatId, name);
        Set<User> users = new HashSet<>();
        users.add(passedUser);
        returnedChat.setChatMembers(users);
        Mockito.when(chatRepository.getById(chatId)).thenReturn(returnedChat);
        Mockito.when(chatRepository.save(returnedChat)).thenReturn(null);

        boolean actualResult = chatService.leaveChat(passedUser, chatId);

        assertTrue(actualResult);
    }

    @Test
    void leaveChatFailedUserNotInChat() {
        User passedUser = new User(userId, firstName, lastName);
        Chat returnedChat = new Chat(chatId, name);
        returnedChat.setChatMembers(new HashSet<>());
        Mockito.when(chatRepository.getById(chatId)).thenReturn(returnedChat);

        boolean actualResult = chatService.leaveChat(passedUser, chatId);

        assertFalse(actualResult);
    }

    @Test
    void isUserInPrivateChatTrue() {
        User passedUser = new User(userId, firstName, lastName);
        Chat returnedChat = new Chat(chatId, name);
        Set<User> users = new HashSet<>();
        users.add(passedUser);
        returnedChat.setChatMembers(users);
        Mockito.when(chatRepository.existsById(chatId)).thenReturn(true);
        Mockito.when(chatRepository.getById(chatId)).thenReturn(returnedChat);

        boolean actualResult = chatService.isUserInPrivateChat(passedUser, chatId);

        assertTrue(actualResult);
    }

    @Test
    void isUserInPrivateChatFalsePrivateChatNotExist() {
        User passedUser = new User(userId, firstName, lastName);
        Mockito.when(chatRepository.existsById(chatId)).thenReturn(false);

        boolean actualResult = chatService.isUserInPrivateChat(passedUser, chatId);

        assertFalse(actualResult);
    }

    @Test
    void isUserInPrivateChatFalseUserNotInPrivateChat() {
        User passedUser = new User(userId, firstName, lastName);
        Chat returnedChat = new Chat(chatId, name);
        returnedChat.setChatMembers(new HashSet<>());
        Mockito.when(chatRepository.existsById(chatId)).thenReturn(true);
        Mockito.when(chatRepository.getById(chatId)).thenReturn(returnedChat);

        boolean actualResult = chatService.isUserInPrivateChat(passedUser, chatId);

        assertFalse(actualResult);
    }

    @Test
    void isPrivateChatExistFalseChatIdIsNull() {
        assertFalse(chatService.isPrivateChatExist(null));
    }
}
