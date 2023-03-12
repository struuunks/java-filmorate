package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@AllArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaStorage;

    public List<Mpa> getAllMpa(){
        return mpaStorage.getAllMpa();
    }

    public Mpa getMpaById(Long id){
        try {
            mpaStorage.getMpaById(id);
        } catch (DataAccessException e){
            throw new IdNotFoundException("Рейтинг с айди " + " не найден");
        }
        return mpaStorage.getMpaById(id);
    }
}
