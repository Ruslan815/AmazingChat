package ru.cft.team2.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.team2.chat.error.ErrorHandler;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.Message;
import ru.cft.team2.chat.model.MessageView;
import ru.cft.team2.chat.service.MessageService;
import ru.cft.team2.chat.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/message")
    public ResponseEntity<?> create(@RequestBody Message someMessage) {
        ValidationResult returnedRequestStatus = ErrorHandler.validateMessage(someMessage, userService.isUserExist(someMessage.getUserId()));
        if (returnedRequestStatus == ValidationResult.NO_ERROR) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            someMessage.setTime(formatter.format(new Date()));
            return ResponseEntity.ok(messageService.create(someMessage));
        }
        return ResponseEntity.internalServerError().body(returnedRequestStatus);
    }

    @GetMapping("/messages")
    public List<MessageView> read() {
        return messageService.getAll();
    }
}
