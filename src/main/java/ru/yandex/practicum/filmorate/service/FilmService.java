package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.validator.FilmValidator;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@RequiredArgsConstructor
public class FilmService implements FilmStorage {

    final FilmDbStorage filmDbStorage;
    final UserDbStorage userDbStorage;
    final FilmValidator validator;


    @Override
    public Collection<Film> getAllFilms() {
        return filmDbStorage.getAllFilms();
    }

    @Override
    public Film getFilmById(Long id) {
        try {
            filmDbStorage.getFilmById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IdNotFoundException("Фильм с айди " + id + " не найден");
        }

        return filmDbStorage.getFilmById(id);
    }

    @Override
    public Film createFilm(Film film) {
        validator.validate(film);
        return filmDbStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            filmDbStorage.getFilmById(film.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new IdNotFoundException("Фильм с айди " + film.getId() + " не найден");
        }

        validator.validate(film);
        return filmDbStorage.updateFilm(film);
    }

    public void likeFilm(Long filmId, Long userId) {
        try {
            filmDbStorage.getFilmById(filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new IdNotFoundException("Фильм с айди " + filmId + " не найден");
        }

        if (userDbStorage.getUserById(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        }

        filmDbStorage.likeFilm(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        try {
            filmDbStorage.getFilmById(filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new IdNotFoundException("Фильм с айди " + filmId + " не найден");
        }

        if (userDbStorage.getUserById(userId) == null) {
            throw new IdNotFoundException("Пользователь с айди " + userId + " не найден");
        }

        filmDbStorage.deleteLike(filmId, userId);
    }

    public List<Film> getMostLikedFilms(Integer count) {
        return filmDbStorage.getMostLikedFilms(count);
    }
}
