package ru.yandex.practicum.filmorate.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@AllArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    public List<Genre> getAllGenres() {
        String sql = "select * from genres";

        return jdbcTemplate.query(sql, genreMapper);
    }

    public Genre getGenreById(Long id) {
        String sql = "select * from genres where genre_id = ?";

        return jdbcTemplate.queryForObject(sql, genreMapper, id);
    }

    public Set<Genre> getFilmGenresByFilmId(Long id) {
        String sql = "select * from genres " +
                "inner join film_genre fg on genres.genre_id = fg.genre_id " +
                "where fg.film_id  = ? ";

        return new HashSet<>(jdbcTemplate.query(sql, genreMapper, id));
    }
}
