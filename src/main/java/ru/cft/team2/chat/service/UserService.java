package ru.cft.team2.chat.service;

import ru.cft.team2.chat.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final Map<Integer, User> userMap = new HashMap<>();
    private Integer idCounter = 0;

    public User create(User someUser) {

        someUser.setId(idCounter);
        userMap.put(idCounter++, someUser);
        return someUser;
    }

    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    public User get(int userId) {
        return userMap.get(userId);
    }

    public User update(User someUser, int userId) {
        if (userMap.containsKey(userId)) {
            someUser.setId(userId);
            userMap.put(userId, someUser);
            return someUser;
        }
        return null;
    }
}
