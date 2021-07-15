package ru.cft.team2.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.*;
import ru.cft.team2.chat.parser.RSSFeedParser;
import ru.cft.team2.chat.service.ChatService;
import ru.cft.team2.chat.service.UserService;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Api(tags = "Чаты")
@RestController
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/chat")
    @ApiOperation(
            value = "Создать чат",
            notes = "Создается чат с указанным названием и подпиской на RSS ленту (необязательно)"
    )
    public ResponseEntity<?> create(
            @ApiParam(value = "Название чата, ссылка на RSS ленту (необязательно)", required = true)
            @RequestBody Chat someChat
    ) {
        ResponseEntity responseEntity;
        ChatView response;
        try {
            if(someChat.getRssLink() != null) {
                RSSFeedParser parser = new RSSFeedParser(someChat.getRssLink());
                parser.readFeed();
            }
            response = chatService.create(someChat);
            responseEntity = ResponseEntity.ok(response);
        } catch (MalformedURLException e) {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.MALFORMED_RSS_LINK);
        } catch (IOException e) {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.URL_CONNECTION_FAILED);
        } catch (XMLStreamException e) {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.UNKNOWN_ERROR);
        } catch (Exception e) {
            responseEntity = ResponseEntity.internalServerError().body(ValidationResult.CHAT_NAME_NOT_FOUND);
        }
        return responseEntity;
    }

    @GetMapping("/chats")
    @ApiOperation(
            value = "Получить все чаты",
            notes = "Получает список всех чатов и возвращает их"
    )
    public List<ChatView> get() {
        return chatService.getAll();
    }

    @PostMapping("/chat/enter")
    @ApiOperation(
            value = "Войти в чат",
            notes = "Регистрирует нового пользователя в чате"
    )
    public ResponseEntity<?> enterChat(
            @ApiParam(value = "Идентификатор пользователя и идентификатор чата, в который хочет присоединиться пользователь",
                    required = true)
            @RequestBody ChatMember chatMember
    ) {
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
    @ApiOperation(
            value = "Выйти из чата",
            notes = "Удаляет пользователя из чата"
    )
    public ResponseEntity<?> leaveChat(
            @ApiParam(value = "Идентификатор пользователя и идентификатор чата, из которого нужно удалить пользователя",
                    required = true)
            @RequestBody ChatMember chatMember
    ) {
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
