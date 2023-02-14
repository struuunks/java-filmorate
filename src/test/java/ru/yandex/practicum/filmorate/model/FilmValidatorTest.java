package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilmValidatorTest {

    private static final String STRING_LENGTH
            = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567891234567890" +
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567891234567890" +
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567891234567890";

    private FilmValidator filmValidator;
    private Film film;

    @BeforeEach
    void beforeEach() {
        filmValidator = new FilmValidator();
        film = new Film(1L, "Name", "Description", LocalDate.of(1999, 1, 1), 60);
    }

    @Test
    void validateCreateFilmTest() {
        filmValidator.validate(film);
        assertEquals("Description", film.getDescription());
        assertEquals("1999-01-01", film.getReleaseDate().toString());
        assertEquals("Name", film.getName());
        assertEquals(1, film.getId());
        assertEquals(60, film.getDuration().intValue());
    }

    @Test
    void validateFilmDurationTest() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmValidator.validate(film));
    }

    @Test
    void validateNameTest() {
        film.setName("");
        assertThrows(ValidationException.class, () -> filmValidator.validate(film));
    }

    @Test
    void validateDescriptionTest() {
        film.setDescription(STRING_LENGTH);
        assertThrows(ValidationException.class, () -> filmValidator.validate(film));
    }

    @Test
    void validateReleaseDateTest() {
        film.setReleaseDate(LocalDate.of(1555, 12, 5));
        assertThrows(ValidationException.class, () -> filmValidator.validate(film));
    }
}