package ru.cft.team2.chat.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import ru.cft.team2.chat.error.ValidationResult;
import ru.cft.team2.chat.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    UserController userController;
    static Integer usersInDatabase = 0;

    @Test
    void create() {
        User expectedUser = new User(usersInDatabase + 1, "Cat", "Dog");
        User actualUser = new User(null, "Cat", "Dog");

        ResponseEntity<?> actual = userController.create(actualUser); usersInDatabase++;

        assertEquals(ResponseEntity.ok(expectedUser), actual);
    }

    @Test
    void createWithoutFirstname() {
        User actualUser = new User(null, null, "Dog");

        ResponseEntity<?> actual = userController.create(actualUser);

        assertEquals(ResponseEntity.internalServerError().body(ValidationResult.FIRSTNAME_NOT_FOUND), actual);
    }

    @Test
    void createWithoutLastname() {
        User actualUser = new User(null, "Cat", null);

        ResponseEntity<?> actual = userController.create(actualUser);

        assertEquals(ResponseEntity.internalServerError().body(ValidationResult.LASTNAME_NOT_FOUND), actual);
    }

    @Test
    void createWithoutFirstnameAndLastname() {
        User actualUser = new User(null, null, null);

        ResponseEntity<?> actual = userController.create(actualUser);

        assertEquals(ResponseEntity.internalServerError().body(ValidationResult.FIRSTNAME_AND_LASTNAME_NOT_FOUND), actual);
    }

    @Test
    void read() {
        User firstUser = new User(null, "firstName1", "lastName1");
        User secondUser = new User(null, "firstName2", "lastName2");
        userController.create(firstUser); usersInDatabase++;
        userController.create(secondUser); usersInDatabase++;
        List<User> expectedList = new ArrayList<>();
        expectedList.add(firstUser);
        expectedList.add(secondUser);

        List<User> actualList = userController.read();

        assertEquals(expectedList, actualList);
    }

    @Test
    void readEmptyList() {
        List<User> expectedList = new ArrayList<>();

        List<User> actualList = userController.read();

        assertEquals(expectedList, actualList);
    }

    /*@Test
    void get() {
    }

    @Test
    void update() {
    }*/
}