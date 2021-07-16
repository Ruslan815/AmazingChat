package ru.cft.team2.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.AttachRequest;
import ru.cft.team2.chat.service.AttachService;
import ru.cft.team2.chat.service.ChatService;
import ru.cft.team2.chat.service.UserService;

import java.io.IOException;

@Controller
public class AttachController {

    private final UserService userService;
    private final ChatService chatService;
    private final AttachService attachService;

    public AttachController(UserService userService, ChatService chatService, AttachService attachService) {
        this.userService = userService;
        this.chatService = chatService;
        this.attachService = attachService;
    }

    @PostMapping("/attach")
    public ResponseEntity<?> handleFileUpload(@ModelAttribute AttachRequest attach) {
        Integer userId = attach.getUserId();
        Integer chatId = attach.getChatId();
        boolean isUserExist = userService.isUserExist(userId);
        boolean isChatExist = (chatId == null || chatService.isPrivateChatExist(chatId));
        boolean isUserInChat = (chatId == null || (isUserExist && chatService.isUserInPrivateChat(userService.getUser(userId), chatId)));
        ResponseEntity<?> responseEntity;
        if (isUserExist && isChatExist && isUserInChat) {
            try {
                responseEntity = ResponseEntity.ok(attachService.create(attach));
            } catch (IOException e) {
                responseEntity = ResponseEntity.internalServerError().body(ValidationResult.UNKNOWN_ERROR);
            }
        } else if (!isUserExist) {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_FOUND);
        } else if (!isChatExist) {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.CHAT_NOT_FOUND);
        } else {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_IN_CHAT);
        }

        return responseEntity;
    }
}
