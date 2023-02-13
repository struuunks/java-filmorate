package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Component
public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film getFilmById(Long id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void likeFilm(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getMostLikedFilms(Integer count);
}
