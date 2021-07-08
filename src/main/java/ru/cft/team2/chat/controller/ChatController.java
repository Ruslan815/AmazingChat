package ru.cft.team2.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.Chat;
import ru.cft.team2.chat.model.ChatView;
import ru.cft.team2.chat.service.ChatService;

@RestController
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> create(@RequestBody Chat someChat) {
        ChatView response;
        try {
            response = chatService.create(someChat);
        } catch (Exception e) {
            return new ResponseEntity<>(ValidationResult.NOT_FOUND_CHAT_NAME, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(response);
    }
}
