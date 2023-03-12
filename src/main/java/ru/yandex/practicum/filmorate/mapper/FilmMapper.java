package ru.yandex.practicum.filmorate.mapper;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@AllArgsConstructor
public class FilmMapper implements RowMapper<Film> {
    GenreDbStorage genreDbStorage;
    MpaDbStorage mpaDbStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder().id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .genres(genreDbStorage.getFilmGenresByFilmId(rs.getLong("film_id")))
                .mpa(mpaDbStorage.getMpaById(rs.getLong("mpa_id")))
                .build();
    }
}
