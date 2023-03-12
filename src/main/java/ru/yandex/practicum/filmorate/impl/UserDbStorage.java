package ru.yandex.practicum.filmorate.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDbStorage implements UserStorage {
    final JdbcTemplate jdbcTemplate;
    final UserMapper userMapper;

    @Override
    public Collection<User> getAllUsers() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, userMapper);
    }

    @Override
    public User getUserById(Long id) {
        try {
            String sql = "select * from users where user_id = ?";
            return jdbcTemplate.queryForObject(sql, userMapper, id);
        } catch (Throwable e) {
            throw new IdNotFoundException("Пользователь с айди " + id + " не найден");
        }
    }

    @Override
    public User createUser(User user) {
        String sql = "insert into users(email, name, login, birthday) "
                + "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getName());
            ps.setString(3, user.getLogin());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }


    @Override
    public User updateUser(User user) {
        String sql = "update users set email = ?, name = ?, login = ?, birthday = ?"
                + "where user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    public void addFriend(Long userId, Long friendId) {
        String sql = "insert into friends (user_id, friend_id)"
                + "values (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        String sql = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public Collection<User> getFriendsList(Long userId) {
        String sql = "select u.user_id, u.email, u.name, u.login, u.birthday " +
                "from friends as f left join users as u " +
                "on f.friend_id = u.user_id where f.user_id = ?" +
                "order by u.user_id";
        return jdbcTemplate.query(sql, userMapper, userId);
    }

    public List<User> getMutualFriends(Long userId, Long friendId) {
        String sql = "select u.user_id, u.name, u.email, u.login, u.birthday " +
                "from friends as f " +
                "left join users as u on f.friend_id = u.user_id " +
                "where f.user_id = ? " +
                "and f.friend_id in (select friend_id from friends where user_id = ?) ";

        return jdbcTemplate.query(sql, userMapper, userId, friendId);
    }
}
