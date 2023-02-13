package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;


@Service
public class UserService implements UserStorage {
    private InMemoryUserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    @Override
    public Collection<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        return storage.getUserById(id);
    }

    @Override
    public User createUser(User user) {
        return storage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        storage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        storage.deleteFriend(userId, friendId);
    }

    @Override
    public Collection<User> getFriendsList(Long id) {
        return storage.getFriendsList(id);
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        return storage.getMutualFriends(userId, otherUserId);
    }
}
