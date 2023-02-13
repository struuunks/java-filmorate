package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidator;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Getter
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final FilmValidator validator = new FilmValidator();
    private final Map<Long, Film> films = new HashMap<>();
    private static Long id = 0L;

    public Long generateId() {
        return ++id;
    }

    private InMemoryUserStorage userStorage;

    @Autowired
    public InMemoryFilmStorage(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }


    @Override
    public Collection<Film> getAllFilms() {
        log.info("Запрошен список фильмов");
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) {
        log.info("Запрошен фильм с айди " + id);
        if (!films.containsKey(id)) {
            throw new IdNotFoundException("Фильм с айди" + id + "не найден");
        } else {
            return films.get(id);
        }
    }

    @Override
    public Film createFilm(Film film) {
        validator.validate(film);
        log.info("Добавлен новый фильм");
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление данных фильма");
        if (films.get(film.getId()) == null) {
            throw new IdNotFoundException("Фильм с айди" + film.getId() + "не найден");
        } else {
            validator.validate(film);
            films.put(film.getId(), film);
            return film;
        }
    }

    @Override
    public void likeFilm(Long filmId, Long userId) {
        log.info("Добавление лайка фильму с айди " + filmId + " пользователем с айди " + userId);
        if (films.get(filmId) == null) {
            throw new IdNotFoundException("Фильм с айди " + filmId + " не найден");
        } else if (userStorage.getUsers().get(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else {
            films.get(filmId).getLikes().add(userId);
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        log.info("Удаление лайка у фильма с айди " + filmId + " пользователем с айди " + userId);
        if (films.get(filmId) == null) {
            throw new IdNotFoundException("Фильм с айди " + filmId + " не найден");
        } else if (userStorage.getUsers().get(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        } else {
            films.get(filmId).getLikes().remove(userId);
        }
    }

    @Override
    public List<Film> getMostLikedFilms(Integer count) {
        log.info("Запрошен лист фильмов с наибольшим количеством лайков");
        return films.values()
                .stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
