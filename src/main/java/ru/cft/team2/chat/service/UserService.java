package ru.cft.team2.chat.service;

import ru.cft.team2.chat.model.User;
import org.springframework.stereotype.Service;
import ru.cft.team2.chat.repository.UserRepository;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User someUser) {
        return userRepository.save(someUser);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User get(int userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    public User update(User someUser, int userId) throws Exception {
        if (userRepository.existsById(userId)) {
            someUser.setId(userId);
            return userRepository.save(someUser);
        }
        throw new Exception();
    }

    public boolean isUserExist(Integer userId) {
        if (userId == null) return false;
        return userRepository.existsById(userId);
    }
}