package ru.cft.team2.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.Chat;
import ru.cft.team2.chat.model.ChatMember;
import ru.cft.team2.chat.model.ChatView;
import ru.cft.team2.chat.service.ChatService;
import ru.cft.team2.chat.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
public class ChatControllerTest {

    @Autowired
    private ChatController chatController;

    @MockBean
    private ChatService chatService;

    @MockBean
    private UserService userService;

    private final Integer userId = 1;
    private final Integer chatId = 1;
    private final String name = "someName";
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createSuccessfulPrivateChat() {
        Chat passedChat = new Chat(chatId, name);
        ChatView expectedChat = new ChatView(chatId, name);
        ResponseEntity expectedResponse = ResponseEntity.ok(expectedChat);
        try {
            Mockito.when(chatService.create(passedChat)).thenReturn(expectedChat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity actualResponse = chatController.create(passedChat);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void createFailedChatNameNotFound() {
        Chat passedChat = new Chat(chatId, name);
        ResponseEntity expectedResponse = ResponseEntity.internalServerError().body(ValidationResult.CHAT_NAME_NOT_FOUND);
        try {
            Mockito.when(chatService.create(passedChat)).thenThrow(Exception.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity actualResponse = chatController.create(passedChat);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void enterChatSuccessful() {
        ChatMember chatMember = new ChatMember(userId, chatId);
        ResponseEntity expectedResponse = null;
        try {
            JsonNode jsonResponse = objectMapper.readTree("{\"enterStatus\": \"You successful entered the chat №" + chatId + "\"}");
            expectedResponse = ResponseEntity.ok(jsonResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Mockito.when(userService.isUserExist(userId)).thenReturn(true);
        Mockito.when(chatService.isPrivateChatExist(chatId)).thenReturn(true);
        Mockito.when(chatService.enterChat(Mockito.any(), eq(chatId))).thenReturn(true);

        ResponseEntity actualResponse = chatController.enterChat(chatMember);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void enterChatFailedUserNotFound() {
        ChatMember chatMember = new ChatMember(userId, chatId);
        ResponseEntity expectedResponse = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_FOUND);
        Mockito.when(userService.isUserExist(userId)).thenReturn(false);

        ResponseEntity actualResponse = chatController.enterChat(chatMember);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void enterChatFailedChatNotFound() {
        ChatMember chatMember = new ChatMember(userId, chatId);
        ResponseEntity expectedResponse = ResponseEntity.internalServerError().body(ValidationResult.CHAT_NOT_FOUND);
        Mockito.when(userService.isUserExist(userId)).thenReturn(true);
        Mockito.when(chatService.isPrivateChatExist(chatId)).thenReturn(false);


        ResponseEntity actualResponse = chatController.enterChat(chatMember);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void enterChatFailedUserAlreadyInChat() {
        ChatMember chatMember = new ChatMember(userId, chatId);
        ResponseEntity expectedResponse = ResponseEntity.internalServerError().body(ValidationResult.USER_ALREADY_IN_CHAT);
        Mockito.when(userService.isUserExist(userId)).thenReturn(true);
        Mockito.when(chatService.isPrivateChatExist(chatId)).thenReturn(true);
        Mockito.when(chatService.enterChat(Mockito.any(), eq(chatId))).thenReturn(false);

        ResponseEntity actualResponse = chatController.enterChat(chatMember);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void leaveChatSuccessful() {
        ChatMember chatMember = new ChatMember(userId, chatId);
        ResponseEntity expectedResponse = null;
        try {
            JsonNode jsonResponse = objectMapper.readTree("{\"leaveStatus\": \"You successful left the chat №" + chatId + "\"}");
            expectedResponse = ResponseEntity.ok(jsonResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Mockito.when(userService.isUserExist(userId)).thenReturn(true);
        Mockito.when(chatService.isPrivateChatExist(chatId)).thenReturn(true);
        Mockito.when(chatService.leaveChat(Mockito.any(), eq(chatId))).thenReturn(true);

        ResponseEntity actualResponse = chatController.leaveChat(chatMember);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void leaveChatFailedUserNotFound() {
        ChatMember chatMember = new ChatMember(userId, chatId);
        ResponseEntity expectedResponse = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_FOUND);
        Mockito.when(userService.isUserExist(userId)).thenReturn(false);

        ResponseEntity actualResponse = chatController.leaveChat(chatMember);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void leaveChatFailedChatNotFound() {
        ChatMember chatMember = new ChatMember(userId, chatId);
        ResponseEntity expectedResponse = ResponseEntity.internalServerError().body(ValidationResult.CHAT_NOT_FOUND);
        Mockito.when(userService.isUserExist(userId)).thenReturn(true);
        Mockito.when(chatService.isPrivateChatExist(chatId)).thenReturn(false);


        ResponseEntity actualResponse = chatController.leaveChat(chatMember);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void leaveChatFailedUserNotInChat() {
        ChatMember chatMember = new ChatMember(userId, chatId);
        ResponseEntity expectedResponse = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_IN_CHAT);
        Mockito.when(userService.isUserExist(userId)).thenReturn(true);
        Mockito.when(chatService.isPrivateChatExist(chatId)).thenReturn(true);
        Mockito.when(chatService.enterChat(Mockito.any(), eq(chatId))).thenReturn(false);

        ResponseEntity actualResponse = chatController.leaveChat(chatMember);

        assertEquals(expectedResponse, actualResponse);
    }
}
