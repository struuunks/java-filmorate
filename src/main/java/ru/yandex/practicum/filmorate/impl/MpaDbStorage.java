package ru.yandex.practicum.filmorate.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@AllArgsConstructor
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    public List<Mpa> getAllMpa(){
        String sql = "select * from mpa";
        return jdbcTemplate.query(sql, mpaMapper);
    }

    public Mpa getMpaById(Long id){
        String sql = "select * from mpa where mpa_id = ?";
        return jdbcTemplate.queryForObject(sql, mpaMapper, id);
    }
}
