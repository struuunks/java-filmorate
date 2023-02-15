package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidator;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Service
@Slf4j
public class UserService implements UserStorage {
    InMemoryUserStorage storage;
    final UserValidator validator= new UserValidator();

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Запрошен список пользователей");
        return storage.getAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        log.info("Запрошен пользователь с айди " + id);
        return storage.getUserById(id);
    }

    @Override
    public User createUser(User user) {
        log.info("Добавлен новый пользователь");
        validator.validate(user);
        return storage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление данных пользователя");
        validator.validate(user);
        return storage.updateUser(user);
    }

    public void addFriend(Long userId, Long friendId) {
        if (storage.getUsers().get(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else if (storage.getUsers().get(friendId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + friendId + " не найден");
        } else {
            log.info("Пользователи с айди " + userId + " и " + friendId + " теперь друзья");
            storage.getUsers().get(userId).getFriends().add(friendId);
            storage.getUsers().get(friendId).getFriends().add(userId);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (storage.getUsers().get(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else if (storage.getUsers().get(friendId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + friendId + " не найден");
        } else {
            log.info("Пользователи с айди " + userId + " и " + friendId + " больше не друзья");
            storage.getUsers().get(userId).getFriends().remove(friendId);
            storage.getUsers().get(friendId).getFriends().remove(userId);
        }
    }

    public Collection<User> getFriendsList(Long userId) {
        log.info("Запрошен лист друзей пользователя с айди " + userId);
        ArrayList<User> friends = new ArrayList<>();
        if (storage.getUsers().containsKey(userId)) {
            for (Long friendId : storage.getUsers().get(userId).getFriends()) {
                friends.add(storage.getUsers().get(friendId));
            }
        } else {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        }
        return friends;
    }

    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        log.info("Запрошен лист общих друзей пользователей с айди " + userId + " и " + otherUserId);
        if (storage.getUsers().get(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else if (storage.getUsers().get(otherUserId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + otherUserId + " не найден");
        } else {
            User user = storage.getUsers().get(userId);
            User friendsUser = storage.getUsers().get(otherUserId);

            return user.getFriends().stream()
                    .filter(f -> friendsUser.getFriends().contains(f))
                    .map(this::getUserById)
                    .collect(Collectors.toList());
        }
    }
}
