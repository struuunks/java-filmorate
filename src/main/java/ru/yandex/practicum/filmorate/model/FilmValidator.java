package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {
    public void validate(Film film) {
        if (film.getName().isEmpty()) {
            log.warn("Название фильма пустое");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.warn("Описание фильма более 200 символов");
            throw new ValidationException("Описание фильма не может содержать более 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза ранее 28.12.1895");
            throw new ValidationException("Дата релиза не может быть ранее 28.12.1895г.");
        }

        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма отрицательна");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
