package ru.cft.team2.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.Attach;
import ru.cft.team2.chat.model.AttachView;
import ru.cft.team2.chat.service.ChatService;
import ru.cft.team2.chat.service.UserService;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class AttachController {

    private final UserService userService;
    private final ChatService chatService;

    public AttachController(UserService userService, ChatService chatService) {
        this.userService = userService;
        this.chatService = chatService;
    }

    @PostMapping("/attach")
    public ResponseEntity<?> handleFileUpload(@ModelAttribute Attach attach) throws IOException {
        Integer userId = attach.getUserId();
        Integer chatId = attach.getChatId();
        boolean isUserExist = userService.isUserExist(userId);
        boolean isChatExist = (chatId == null || chatService.isPrivateChatExist(chatId));
        boolean isUserInChat = (chatId == null || (isUserExist && chatService.isUserInPrivateChat(userService.getUser(userId), chatId)));
        ResponseEntity<?> responseEntity;
        if (isUserExist && isChatExist && isUserInChat) {
            AttachView attachView = new AttachView(attach);
            responseEntity = ResponseEntity.ok(attachView);
            attach.getFile().transferTo(new File(System.getProperty("user.dir") + File.separator + attachView.getFileName()));
        } else if (!isUserExist) {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_FOUND);
        } else if (!isChatExist) {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.CHAT_NOT_FOUND);
        } else {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_IN_CHAT);
        }

        return responseEntity;
    }

    @GetMapping("/attach/{fileName}")
    public ResponseEntity<?> getFile(@PathVariable(name = "fileName") String fileName) {
        Path path = Paths.get(System.getProperty("user.dir") + File.separator + fileName);
        String fileType = fileName.substring(fileName.lastIndexOf('.'));
        
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile result = new MockMultipartFile(fileName,
                fileName, contentType, content);

        return ResponseEntity.ok(result);
    }

}
