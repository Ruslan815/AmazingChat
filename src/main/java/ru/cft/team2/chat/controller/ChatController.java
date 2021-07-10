package ru.cft.team2.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.Chat;
import ru.cft.team2.chat.model.ChatMember;
import ru.cft.team2.chat.model.ChatView;
import ru.cft.team2.chat.service.ChatService;
import ru.cft.team2.chat.service.UserService;

import java.util.List;

@RestController
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> create(@RequestBody Chat someChat) {
        ChatView response;
        try {
            response = chatService.create(someChat);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ValidationResult.CHAT_NAME_NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chats")
    public List<ChatView> get() {
        return chatService.getAll();
    }

    @PostMapping("/chat/enter")
    public ResponseEntity<?> enterChat(@RequestBody ChatMember chatMember) {
        ResponseEntity response;
        Integer userId = chatMember.getUserId();
        Integer chatId = chatMember.getChatId();

        if (!userService.isUserExist(userId)) {
            response = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_FOUND);
        } else if (!chatService.isPrivateChatExist(chatId)) {
            response = ResponseEntity.internalServerError().body(ValidationResult.CHAT_NOT_FOUND);
        } else {
            if (chatService.enterChat(userService.getUser(userId), chatId)) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonResponse = objectMapper.readTree("{\"enterStatus\": \"You successful entered the chat №" + chatId + "\"}");
                    response = ResponseEntity.ok(jsonResponse);
                } catch (JsonProcessingException e) {
                    response = ResponseEntity.internalServerError().body(ValidationResult.UNKNOWN_ERROR);
                }
            } else {
                response = ResponseEntity.internalServerError().body(ValidationResult.USER_ALREADY_IN_CHAT);
            }
        }
        return response;
    }

    @PostMapping("/chat/leave")
    public ResponseEntity<?> leaveChat(@RequestBody ChatMember chatMember) {
        ResponseEntity response;
        Integer userId = chatMember.getUserId();
        Integer chatId = chatMember.getChatId();

        if (!userService.isUserExist(userId)) {
            response = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_FOUND);
        } else if (!chatService.isPrivateChatExist(chatId)) {
            response = ResponseEntity.internalServerError().body(ValidationResult.CHAT_NOT_FOUND);
        } else {
            if (chatService.leaveChat(userService.getUser(userId), chatId)) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonResponse = objectMapper.readTree("{\"leaveStatus\": \"You successful left the chat №" + chatId + "\"}");
                    response = ResponseEntity.ok(jsonResponse);
                } catch (JsonProcessingException e) {
                    response = ResponseEntity.internalServerError().body(ValidationResult.UNKNOWN_ERROR);
                }
            } else {
                response = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_IN_CHAT);
            }
        }
        return response;
    }
}
