package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.validator.UserValidator;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserService implements UserStorage {
    final UserDbStorage userDbStorage;
    final UserValidator validator;


    @Override
    public Collection<User> getAllUsers() {
        try {
            return userDbStorage.getAllUsers();
        } catch (IncorrectResultSizeDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public User getUserById(Long id) {
        if (userDbStorage.getUserById(id) == null) {
            throw new IdNotFoundException("Пользователь с айди " + id + " не найден");
        }
        return userDbStorage.getUserById(id);
    }

    @Override
    public User createUser(User user) {
        validator.validate(user);
        return userDbStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        if (userDbStorage.getUserById(user.getId()) == null) {
            throw new IdNotFoundException("Пользователь с айди " + user.getId() + " не найден");
        }
        validator.validate(user);
        return userDbStorage.updateUser(user);
    }

    public void addFriend(Long userId, Long friendId) {
        if (userDbStorage.getUserById(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else if (userDbStorage.getUserById(friendId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + friendId + " не найден");
        }

        userDbStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (userDbStorage.getUserById(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else if (userDbStorage.getUserById(friendId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + friendId + " не найден");
        }

        userDbStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getFriendsList(Long userId) {
        if (userDbStorage.getUserById(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        }

        return userDbStorage.getFriendsList(userId);
    }

    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        if (userDbStorage.getUserById(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else if (userDbStorage.getUserById(otherUserId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + otherUserId + " не найден");
        }

        return userDbStorage.getMutualFriends(userId, otherUserId);
    }
}
