package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidator;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Service
@Slf4j
public class FilmService implements FilmStorage {

    InMemoryFilmStorage filmStorage;
    InMemoryUserStorage userStorage;
    final FilmValidator validator = new FilmValidator();

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    @Override
    public Collection<Film> getAllFilms() {
        log.info("Запрошен список фильмов");
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilmById(Long id) {
        log.info("Запрошен фильм с айди " + id);
        return filmStorage.getFilmById(id);
    }

    @Override
    public Film createFilm(Film film) {
        validator.validate(film);
        log.info("Добавлен новый фильм");
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление данных фильма");
        validator.validate(film);
        return filmStorage.updateFilm(film);
    }

    public void likeFilm(Long filmId, Long userId) {
        log.info("Добавление лайка фильму с айди " + filmId + " пользователем с айди " + userId);
        if (filmStorage.getFilms().get(filmId) == null) {
            throw new IdNotFoundException("Фильм с айди " + filmId + " не найден");
        } else if (userStorage.getUsers().get(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else {
            filmStorage.getFilms().get(filmId).getLikes().add(userId);
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        log.info("Удаление лайка у фильма с айди " + filmId + " пользователем с айди " + userId);
        if (filmStorage.getFilms().get(filmId) == null) {
            throw new IdNotFoundException("Фильм с айди " + filmId + " не найден");
        } else if (userStorage.getUsers().get(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else {
            filmStorage.getFilms().get(filmId).getLikes().remove(userId);
        }
    }

    public List<Film> getMostLikedFilms(Integer count) {
        log.info("Запрошен лист фильмов с наибольшим количеством лайков");
        return filmStorage.getFilms().values()
                .stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
