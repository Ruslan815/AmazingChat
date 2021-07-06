package ru.cft.team2.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> create(@RequestBody User user) {
        ValidationResult returnedRequestStatus = ErrorHandler.validateUser(user);
        if (returnedRequestStatus != ValidationResult.NO_ERROR) {
            return new ResponseEntity<>(returnedRequestStatus, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userService.create(user), HttpStatus.OK);
    }

    @GetMapping("/users")
    public List<User> read() {
        return userService.getAll();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> get(@PathVariable(name = "userId") int userId) {
        User returnedUser = userService.get(userId);
        return returnedUser == null
                ? new ResponseEntity<>(ValidationResult.USER_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR)
                : new ResponseEntity<>(returnedUser, HttpStatus.OK);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<?> update(@PathVariable(name = "userId") int userId, @RequestBody User user) {
        ValidationResult returnedRequestStatus = ErrorHandler.validateUser(user);
        if (returnedRequestStatus != ValidationResult.NO_ERROR) {
            return new ResponseEntity<>(returnedRequestStatus, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User returnedUser = userService.update(user, userId);
        if (returnedUser == null) {
            return new ResponseEntity<>(ValidationResult.USER_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(returnedUser, HttpStatus.OK);
    }
}