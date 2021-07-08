package ru.cft.team2.chat.controller;

import org.springframework.http.HttpStatus;
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
            return new ResponseEntity<>(ValidationResult.CHAT_NAME_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
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
            response = new ResponseEntity<>(ValidationResult.USER_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (!chatService.isChatExist(chatId)) {
            response = new ResponseEntity<>(ValidationResult.CHAT_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            if (chatService.enterChat(userId, chatId)) {
                response = ResponseEntity.ok("You entered the chat №" + chatId);
            } else {
                response = new ResponseEntity<>(ValidationResult.USER_ALREADY_IN_CHAT, HttpStatus.INTERNAL_SERVER_ERROR);
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
            response = new ResponseEntity<>(ValidationResult.USER_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (!chatService.isChatExist(chatId)) {
            response = new ResponseEntity<>(ValidationResult.CHAT_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            if (chatService.leaveChat(userId, chatId)) {
                response = ResponseEntity.ok("You left the chat №" + chatId);
            } else {
                response = new ResponseEntity<>(ValidationResult.USER_NOT_IN_CHAT, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return response;
    }
}
