package ru.cft.team2.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.User;
import ru.cft.team2.chat.service.UserService;
import ru.cft.team2.chat.error.ErrorHandler;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> create(@RequestBody User user) {
        ValidationResult returnedRequestStatus = ErrorHandler.validateUser(user);
        if (returnedRequestStatus != ValidationResult.NO_ERROR) {
            return new ResponseEntity<>(returnedRequestStatus, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(userService.create(user));
    }

    @GetMapping("/users")
    public List<User> read() {
        return userService.getAll();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> get(@PathVariable(name = "userId") int userId) {
        User returnedUser;
        try {
            returnedUser = userService.get(userId);
        } catch (Exception e) {
            return new ResponseEntity<>(ValidationResult.USER_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(returnedUser);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<?> update(@PathVariable(name = "userId") int userId, @RequestBody User user) {
        ResponseEntity<?> response;
        ValidationResult returnedRequestStatus = ErrorHandler.validateUser(user);
        if (returnedRequestStatus == ValidationResult.NO_ERROR) {
            User returnedUser;
            try {
                returnedUser = userService.update(user, userId);
                response = ResponseEntity.ok(returnedUser);
            } catch (Exception e) {
                response = new ResponseEntity<>(ValidationResult.USER_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            response = new ResponseEntity<>(returnedRequestStatus, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}