package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Component
@Getter
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    final Map<Long, Film> films = new HashMap<>();
    static Long id = 0L;

    public Long generateId() {
        return ++id;
    }


    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new IdNotFoundException("Фильм с айди" + id + "не найден");
        } else {
            return films.get(id);
        }
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.get(film.getId()) == null) {
            throw new IdNotFoundException("Фильм с айди" + film.getId() + "не найден");
        } else {
            films.put(film.getId(), film);
            return film;
        }
    }
}
