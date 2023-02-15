package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleIdNotFound(final IdNotFoundException e) {
        log.info("Ошибка 400, неверный айди в поле запроса");
        return new ResponseEntity<>(Map.of("Ошибка параметра айди", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidation(final ValidationException e) {
        log.info("Ошибка 500, ошибка валидации");
        return new ResponseEntity<>(Map.of("Ошибка валидации", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
