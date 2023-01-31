package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserValidator validator = new UserValidator();
    private final Map<Long, User> users = new HashMap<>();
    private static Long id = 0L;
    public Long generateId(){
        return ++id;
    }

    @GetMapping
    public Collection<User> getAllUsers(){
        log.info("Запрошен список пользователей");
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        log.info("Добавлен новый пользователь");
        validator.validate(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Обновление данных пользователя");
        if(users.get(user.getId()) == null){
                throw new IdNotFoundException("Пользователь с айди " + user.getId() + " не найден");
        }else {
            validator.validate(user);
            users.put(user.getId(), user);
            return user;
        }
    }
}
