package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Component
@Getter
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    final Map<Long, User> users = new HashMap<>();
    static Long id = 0L;

    public Long generateId() {
        return ++id;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        if (users.get(id) == null) {
            throw new IdNotFoundException("Пользователь с айди " + id + " не найден");
        } else {
            return users.get(id);
        }
    }

    @Override
    public User createUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()) == null) {
            throw new IdNotFoundException("Пользователь с айди " + user.getId() + " не найден");
        } else {
            users.put(user.getId(), user);
            return user;
        }
    }
}
