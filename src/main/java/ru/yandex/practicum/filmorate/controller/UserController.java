package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private final UserValidator validator = new UserValidator();
    private final Map<Integer, User> users = new HashMap<>();
    private static int id = 0;
    public int generateId(){
        return ++id;
    }

    @GetMapping("/users")
    public Collection<User> getAllUsers(){
        log.info("Запрошен список пользователей");
        return users.values();
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user){
        log.info("Добавлен новый пользователь");
        validator.validate(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        log.info("Обновление данных пользователя");
        if(users.get(user.getId()) == null){
                throw new NullPointerException("Пользователь с таким айди не найден");
        }else {
            validator.validate(user);
            users.put(user.getId(), user);
            return user;
        }
    }
}
