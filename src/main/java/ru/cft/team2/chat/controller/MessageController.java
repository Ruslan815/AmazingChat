package ru.cft.team2.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.team2.chat.error.ErrorHandler;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.Chat;
import ru.cft.team2.chat.model.Message;
import ru.cft.team2.chat.service.ChatService;
import ru.cft.team2.chat.service.MessageService;
import ru.cft.team2.chat.service.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
            notes = "Создать сообщение с указанным временем жизни (бесконечность, если не указано)" +
                    " и запланированным временем отправки (текущее, если не указано), после чего отправить его в указанный чат"
    )
    public ResponseEntity<?> create(
            @ApiParam(value = "Сообщение, которое содержит: идентификатор пользователя, " +
                    "идентификатор чата (общий чат, если не указано), текст сообщения, " +
                    "время жизни сообщения (бесконечность, если не указано) и " +
                    "запланированное время отправки (текущее, если не указано)",
                    required = true)
            @RequestBody Message someMessage
    ) {
        Integer userId = someMessage.getUserId();
        Integer chatId = someMessage.getChatId();
        boolean isUserExist = userService.isUserExist(userId);
        boolean isChatExist = (chatId == null || chatService.isPrivateChatExist(chatId));
        boolean isUserInChat = (chatId == null || (isUserExist && chatService.isUserInPrivateChat(userService.getUser(userId), chatId)));

        ValidationResult returnedRequestStatus = ErrorHandler.validateMessage(someMessage, isUserExist, isChatExist, isUserInChat);
        if (returnedRequestStatus != ValidationResult.NO_ERROR) {
            return ResponseEntity.internalServerError().body(returnedRequestStatus);
        }

        long currentTimeInMillis = System.currentTimeMillis();
        long delaySec = 0;
        if (someMessage.getDelaySec() != null) {
            delaySec = someMessage.getDelaySec();
        }
        someMessage.setSendTimeSec(currentTimeInMillis / 1000 + delaySec);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        someMessage.setSendTime(formatter.format(currentTimeInMillis + delaySec * 1000));

        if (chatId == null) {
            someMessage.setUsersWhoDidNotRead(userService.getAllUsers());
        } else {
            Chat chat = chatService.getByChatId(chatId);
            someMessage.setUsersWhoDidNotRead(new ArrayList<>(chat.getChatMembers()));
        }

        return ResponseEntity.ok(messageService.create(someMessage));
    }

    @GetMapping("/messages")
    @ApiOperation(
            value = "Получить все сообщения",
            notes = "Возвращает список всех сообщений из указанного чата"
    )
    public ResponseEntity<?> read(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestParam Integer userId,
            @ApiParam(value = "Идентификатор чата (для общего чата не указывается)")
            @RequestParam(required = false) Integer chatId
    ) {
        boolean isUserExist = userService.isUserExist(userId);
        boolean isChatExist = (chatId == null || chatService.isPrivateChatExist(chatId));
        boolean isUserInChat = (chatId == null || (isUserExist && chatService.isUserInPrivateChat(userService.getUser(userId), chatId)));
        ResponseEntity responseEntity;
        if (isUserExist && isChatExist && isUserInChat) {
            messageService.readMessages(userService.getUser(userId), chatId); // Marks unread messages in this chat for this user as read
            responseEntity = ResponseEntity.ok(messageService.getAllByChatId(chatId));
        } else if (!isUserExist) {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_FOUND);
        } else if (!isChatExist) {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.CHAT_NOT_FOUND);
        } else {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_IN_CHAT);
        }

        return responseEntity;
    }

    @GetMapping("/messages/unread")
    @ApiOperation(
            value = "Получить список непрочитанных сообщений",
            notes = "Возвращает список всех непрочитанных сообщений пользователя из указанного чата"
    )
    public ResponseEntity<?> getUnreadMessages(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestParam Integer userId,
            @ApiParam(value = "Идентификатор чата (для общего чата не указывается)")
            @RequestParam(required = false) Integer chatId
    ) {
        boolean isUserExist = userService.isUserExist(userId);
        boolean isChatExist = (chatId == null || chatService.isPrivateChatExist(chatId));
        boolean isUserInChat = (chatId == null || (isUserExist && chatService.isUserInPrivateChat(userService.getUser(userId), chatId)));
        ResponseEntity responseEntity;
        if (isUserExist && isChatExist && isUserInChat) {
            responseEntity = ResponseEntity.ok(messageService.readMessages(userService.getUser(userId), chatId));
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
