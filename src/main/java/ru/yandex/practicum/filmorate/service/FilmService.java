package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService implements FilmStorage {

    private InMemoryFilmStorage storage;

    @Autowired
    public FilmService(InMemoryFilmStorage storage) {
        this.storage = storage;
    }


    @Override
    public Collection<Film> getAllFilms() {
        return storage.getAllFilms();
    }

    @Override
    public Film getFilmById(Long id) {
        return storage.getFilmById(id);
    }

    @Override
    public Film createFilm(Film film) {
        return storage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }

    @Override
    public void likeFilm(Long filmId, Long userId) {
        storage.likeFilm(filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        storage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getMostLikedFilms(Integer count) {
        return storage.getMostLikedFilms(count);
    }
}
