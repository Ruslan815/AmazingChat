package ru.cft.team2.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.team2.chat.error.ErrorHandler;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.Message;
import ru.cft.team2.chat.model.MessageView;
import ru.cft.team2.chat.service.ChatService;
import ru.cft.team2.chat.service.MessageService;
import ru.cft.team2.chat.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;
    private final ChatService chatService;

    public MessageController(MessageService messageService, UserService userService, ChatService chatService) {
        this.messageService = messageService;
        this.userService = userService;
        this.chatService = chatService;
    }

    @PostMapping("/message")
    public ResponseEntity<?> create(@RequestBody Message someMessage) {
        Integer userId = someMessage.getUserId();
        Integer chatId = someMessage.getChatId();
        boolean isUserExist = userService.isUserExist(userId);
        boolean isChatExist = true;
        boolean isUserInChat = true;
        if (chatId != null) {
            isChatExist = chatService.isPrivateChatExist(chatId);
            isUserInChat = chatService.isUserInPrivateChat(userId, chatId);
        }

        ValidationResult returnedRequestStatus = ErrorHandler.validateMessage(someMessage, isUserExist, isChatExist, isUserInChat);
        if (returnedRequestStatus != ValidationResult.NO_ERROR) {
            return ResponseEntity.internalServerError().body(returnedRequestStatus);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        someMessage.setTime(formatter.format(new Date()));
        return ResponseEntity.ok(messageService.create(someMessage));
    }

    @GetMapping("/messages")
    public List<MessageView> read() {
        return messageService.getAll();
    }
}
