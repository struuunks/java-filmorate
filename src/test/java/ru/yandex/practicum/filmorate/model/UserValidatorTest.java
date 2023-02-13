package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserValidatorTest {

    private UserValidator userValidator;
    private User user;

    @BeforeEach
    void beforeEach() {
        userValidator = new UserValidator();
        user = new User(1L, "mail@mail.com", "Login", "Name", LocalDate.of(1995, 12, 1), new HashSet<>());
    }

    @Test
    void validateCreatedUserTest() {
        userValidator.validate(user);
        assertEquals(LocalDate.of(1995, 12, 1), user.getBirthday());
        assertEquals("Login", user.getLogin());
        assertEquals("mail@mail.com", user.getEmail());
        assertEquals("Name", user.getName());
        assertEquals(1, user.getId());
    }

    @Test
    void validateEmptyEmailTest() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    void validateEmailWithoutAtTest() {
        user.setEmail("mail.com");
        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    void validateLoginWithSpaceTest() {
        user.setLogin("L o g i n");
        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    void validateLoginIsEmptyTest() {
        user.setLogin("");
        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    void validateNameTest() {
        user.setName("");
        userValidator.validate(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void validateNameWithoutLoginTest() {
        user.setLogin("");
        user.setName("");
        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }

    @Test
    void validateBirthdayTest() {
        user.setBirthday(LocalDate.of(2033, 12, 1));
        assertThrows(ValidationException.class, () -> userValidator.validate(user));
    }
}