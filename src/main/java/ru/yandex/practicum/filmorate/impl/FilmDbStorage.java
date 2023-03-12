package ru.yandex.practicum.filmorate.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private final FilmMapper filmMapper;

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, filmMapper);
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "select * from films where film_id = ?";
        return jdbcTemplate.queryForObject(sql, filmMapper, id);
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "insert into films(name, description, release_date, duration, mpa_id) "
                + "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setString(3, film.getReleaseDate().toString());
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlGenres = "insert into film_genre (film_id, genre_id) values (?, ?)";
                jdbcTemplate.update(sqlGenres, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "update films set  name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?"
                + "where film_id = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (film.getGenres() != null) {
            Set<Genre> genreSet = new HashSet<>(film.getGenres());
            jdbcTemplate.update("delete from film_genre where film_id = ?", film.getId());
            genreSet.stream().sorted(Comparator.comparing(Genre::getId));
            film.setGenres(genreSet);
            for (Genre genre : film.getGenres()) {
                String sqlGenres = "insert into film_genre (film_id, genre_id) values(?, ?)";
                jdbcTemplate.update(sqlGenres, film.getId(), genre.getId());
            }
        }
        return getFilmById(film.getId());
    }

    public void likeFilm(Long filmId, Long userId) {
        String sql = "insert into likes (film_id, user_id)" + "values(?, ?)";

        jdbcTemplate.update(sql, filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        String sql = "delete from likes where film_id = ? and user_id = ?";

        jdbcTemplate.update(sql, filmId, userId);
    }

    public List<Long> getLikes(Long filmId) {
        String sql = "select user_id from likes where film_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }

    public List<Film> getMostLikedFilms(int count) {
        String sql = "select f.* from films f " +
                "left join likes l on f.film_id = l.film_id " +
                "group by  f.film_id " +
                "order by count(l.user_id) desc " +
                "limit ?";

        return jdbcTemplate.query(sql, filmMapper, count);
    }
}
