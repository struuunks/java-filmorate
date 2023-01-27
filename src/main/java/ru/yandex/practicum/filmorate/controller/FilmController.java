package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    private final FilmValidator validator = new FilmValidator();
    private final Map<Integer, Film> films = new HashMap<>();

    private static int id = 0;
    public int generateId(){
        return ++id;
    }

    @GetMapping("/films")
    public Collection<Film> getAllFilms(){
        log.info("Запрошен список фильмов");
        return films.values();
    }


    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film){
        log.info("Добавлен новый фильм");
        validator.validate(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film){
        log.info("Обновление данных фильма");
        if (films.get(film.getId()) == null){
            throw new NullPointerException("Фильм с таким айди не найден");
        } else {
            validator.validate(film);
            films.put(film.getId(), film);
            return film;
        }
    }
}
