package ru.cft.team2.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.Attach;
import ru.cft.team2.chat.model.AttachView;
import ru.cft.team2.chat.service.ChatService;
import ru.cft.team2.chat.service.UserService;

@Controller
public class FileUploadController {

    private final UserService userService;
    private final ChatService chatService;

    public FileUploadController(UserService userService, ChatService chatService) {
        this.userService = userService;
        this.chatService = chatService;
    }

    @PostMapping("/attach")
    public ResponseEntity<?> handleFileUpload(@ModelAttribute Attach attach) {
        Integer userId = attach.getUserId();
        Integer chatId = attach.getChatId();
        boolean isUserExist = userService.isUserExist(userId);
        boolean isChatExist = (chatId == null || chatService.isPrivateChatExist(chatId));
        boolean isUserInChat = (chatId == null || (isUserExist && chatService.isUserInPrivateChat(userService.getUser(userId), chatId)));
        ResponseEntity<?> responseEntity;
        if (isUserExist && isChatExist && isUserInChat) {
            responseEntity = ResponseEntity.ok(new AttachView(attach));
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
