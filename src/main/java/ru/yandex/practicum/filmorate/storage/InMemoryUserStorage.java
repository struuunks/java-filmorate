package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidator;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Getter
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final UserValidator validator = new UserValidator();
    private final Map<Long, User> users = new HashMap<>();
    private static Long id = 0L;

    public Long generateId() {
        return ++id;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Запрошен список пользователей");
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        log.info("Запрошен пользователь с айди " + id);
        if (users.get(id) == null) {
            throw new IdNotFoundException("Пользователь с айди " + id + " не найден");
        } else {
            return users.get(id);
        }
    }

    @Override
    public User createUser(User user) {
        log.info("Добавлен новый пользователь");
        validator.validate(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление данных пользователя");
        if (users.get(user.getId()) == null) {
            throw new IdNotFoundException("Пользователь с айди " + user.getId() + " не найден");
        } else {
            validator.validate(user);
            users.put(user.getId(), user);
            return user;
        }
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (users.get(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else if (users.get(friendId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + friendId + " не найден");
        } else {
            log.info("Пользователи с айди " + userId + " и " + friendId + " теперь друзья");
            users.get(userId).getFriends().add(friendId);
            users.get(friendId).getFriends().add(userId);
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        if (users.get(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else if (users.get(friendId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + friendId + " не найден");
        } else {
            log.info("Пользователи с айди " + userId + " и " + friendId + " больше не друзья");
            users.get(userId).getFriends().remove(friendId);
            users.get(friendId).getFriends().remove(userId);
        }
    }

    @Override
    public Collection<User> getFriendsList(Long id) {
        log.info("Запрошен лист друзей пользователя с айди " + id);
        ArrayList<User> friends = new ArrayList<>();
        if (users.containsKey(id)) {
            for (Long friendId : users.get(id).getFriends()) {
                friends.add(users.get(friendId));
            }
        } else {
            throw new IdNotFoundException("Пользователь с айди " + id + " не найден");
        }
        return friends;
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        log.info("Запрошен лист общих друзей пользователей с айди " + userId + " и " + otherUserId);
        if (users.get(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else if (users.get(otherUserId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + otherUserId + " не найден");
        } else {
            User user = users.get(userId);
            User friendsUser = users.get(otherUserId);

            return user.getFriends().stream()
                    .filter(f -> friendsUser.getFriends().contains(f))
                    .map(this::getUserById)
                    .collect(Collectors.toList());
        }
    }
}
