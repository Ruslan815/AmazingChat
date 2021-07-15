package ru.cft.team2.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.User;
import ru.cft.team2.chat.model.UserView;
import ru.cft.team2.chat.service.UserService;
import ru.cft.team2.chat.error.ErrorHandler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Api(tags = "Пользователи")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    @ApiOperation(
            value = "Создать пользователя",
            notes = "Создается пользователь с указанными именем и фамилией"
    )
    public ResponseEntity<?> create(
            @ApiParam(value = "Пользователь", required = true)
            @RequestBody User user
    ) {
        ValidationResult returnedRequestStatus = ErrorHandler.validateUser(user);
        if (returnedRequestStatus != ValidationResult.NO_ERROR) {
            return ResponseEntity.internalServerError().body(returnedRequestStatus);
        }
        return ResponseEntity.ok(userService.create(user));
    }

    @ApiOperation(
            value = "Получить всех пользователей",
            notes = "Получает список всех пользователей и возвращает их"
    )
    @GetMapping("/users")
    public List<UserView> read() {
        return userService.getAllUserViews();
    }

    @ApiOperation(
            value = "Получить пользователя по идентификатору",
            notes = "Получает на вход идентификатор пользователя и возвращает пользователя с этим идентификатором или возвращает ошибку, если пользователь не найден"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> get(
            @ApiParam(value = "Идентификатор пользователя", required = true, example = "1")
            @PathVariable(name = "userId") int userId
    ) {
        UserView returnedUser;
        try {
            returnedUser = userService.getUserView(userId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_FOUND);
        }
        return ResponseEntity.ok(returnedUser);
    }

    @ApiOperation(
            value = "Обновить пользователя",
            notes = "Получает на вход пользователя и его идентификатор, после чего обновляем данные пользователя"
    )
    @PutMapping("/user/{userId}")
    public ResponseEntity<?> update(
            @ApiParam(value = "Идентификатор пользователя", required = true, example = "1")
            @PathVariable(name = "userId") int userId,

            @ApiParam(value = "Пользователь", required = true)
            @RequestBody User user
    ) {
        ResponseEntity<?> response;
        ValidationResult returnedRequestStatus = ErrorHandler.validateUser(user);
        if (returnedRequestStatus == ValidationResult.NO_ERROR) {
            UserView returnedUser;
            try {
                returnedUser = userService.update(user, userId);
                response = ResponseEntity.ok(returnedUser);
            } catch (Exception e) {
                response = ResponseEntity.internalServerError().body(ValidationResult.USER_NOT_FOUND);
            }
        } else {
            response = ResponseEntity.internalServerError().body(returnedRequestStatus);
        }
        return response;
    }
}