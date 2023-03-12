package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService service;


    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Запрошен список фильмов");
        return service.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Запрошен фильм с айди " + id);
        return service.getFilmById(id);
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Добавлен новый фильм");
        return service.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновление данных фильма");
        return service.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Добавление лайка фильму с айди " + id + " пользователем с айди " + userId);
        service.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Удаление лайка у фильма с айди " + id + " пользователем с айди " + userId);
        service.deleteLike(id, userId);
    }


    @GetMapping("/popular")
    public List<Film> get10MostLikedFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Запрошен лист фильмов с наибольшим количеством лайков");
        return service.getMostLikedFilms(count);
    }
}
