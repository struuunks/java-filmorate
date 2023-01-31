package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmValidator validator = new FilmValidator();
    private final Map<Long, Film> films = new HashMap<>();

    private static Long id = 0L;
    public Long generateId(){
        return ++id;
    }

    @GetMapping
    public Collection<Film> getAllFilms(){
        log.info("Запрошен список фильмов");
        return films.values();
    }


    @PostMapping
    public Film createFilm(@RequestBody Film film){
        log.info("Добавлен новый фильм");
        validator.validate(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film){
        log.info("Обновление данных фильма");
        if (films.get(film.getId()) == null){
            throw new IdNotFoundException("Фильм с айди" + film.getId() + "не найден");
        } else {
            validator.validate(film);
            films.put(film.getId(), film);
            return film;
        }
    }
}
