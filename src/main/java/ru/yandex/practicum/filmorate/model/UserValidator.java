package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

@Slf4j
public class UserValidator {

    public void validate(User user){
        if(user.getEmail().isEmpty() || !user.getEmail().contains("@")){
            log.warn("Электронная почта пуста или не содержит символ @");
            throw new ValidationException("Электронная почта не может быть пустой или не содержать символ @");
        }

        if(user.getLogin().isEmpty() || user.getLogin().contains(" ")){
            log.warn("Логин пуст или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        }

        if(user.getName() == null){
            user.setName(user.getLogin());
            log.info("Введенное имя пользователя было пустым");
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            log.warn("Введенная дата рождения в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
