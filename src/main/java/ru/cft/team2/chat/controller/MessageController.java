package ru.cft.team2.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.team2.chat.error.ErrorHandler;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.Message;
import ru.cft.team2.chat.service.ChatService;
import ru.cft.team2.chat.service.MessageService;
import ru.cft.team2.chat.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;

@Api(tags = "Сообщения")
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
    @ApiOperation(
            value = "Отправить сообщение",
            notes = "Создать сообщение и отправить его в указанный чат"
    )
    public ResponseEntity<?> create(
            @ApiParam(value = "Сообщение, которое содержит: идентификатор пользователя, " +
                    "идентификатор чата (если не указано - общий чат) и текст сообщения",
                    required = true)
            @RequestBody Message someMessage
    ) {
        Integer userId = someMessage.getUserId();
        Integer chatId = someMessage.getChatId();
        boolean isUserExist = userService.isUserExist(userId);
        boolean isChatExist = true;
        boolean isUserInChat = true;
        if (chatId != null) {
            isChatExist = chatService.isPrivateChatExist(chatId);
            isUserInChat = chatService.isUserInPrivateChat(userService.getUser(userId), chatId);
        }

        ValidationResult returnedRequestStatus = ErrorHandler.validateMessage(someMessage, isUserExist, isChatExist, isUserInChat);
        if (returnedRequestStatus != ValidationResult.NO_ERROR) {
            return ResponseEntity.internalServerError().body(returnedRequestStatus);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        someMessage.setSendTime(formatter.format(new Date()));
        return ResponseEntity.ok(messageService.create(someMessage));
    }

    @GetMapping("/messages")
    @ApiOperation(
            value = "Получить все сообщения",
            notes = "Возвращает список всех сообщений из указанного чата"
    )
    public ResponseEntity<?> read(
            @ApiParam(value = "Идентификатор чата (для общего чата не указывается)", required = false)
            @RequestParam(required = false) Integer chatId
    ) {
        if (chatId == null || chatService.isPrivateChatExist(chatId)) {
            return ResponseEntity.ok(messageService.getAllByChatId(chatId));
        }
        return ResponseEntity.internalServerError().body(ValidationResult.CHAT_NOT_FOUND);
    }
}
