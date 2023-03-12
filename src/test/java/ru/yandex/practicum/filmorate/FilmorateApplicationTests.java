package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private Film film1;
    private Film film2;

    @BeforeEach
    public void beforeEach() {
        user1 = new User(1L, "user1@yandex.ru", "userfirst",
                "User First", LocalDate.of(1995, 12, 1));
        user2 = new User(2L, "user2@yandex.ru", "usersecond",
                "User Second", LocalDate.of(1996, 12, 17));
        user3 = new User(3L, "user3@yandex.ru", "userthird",
                "User Third", LocalDate.of(1976, 1, 30));
        user4 = new User(4L, "user4@yandex.ru", "userfourth",
                "User Fourth", LocalDate.of(2005, 5, 13));
        film1 = new Film(1L, "First Film", "Description first film",
                LocalDate.of(2000, 5, 16), 185, new HashSet<>(), mpaStorage.getMpaById(2L));
        film2 = new Film(2L, "Second Film", "Description second film",
                LocalDate.of(2005, 11, 10), 96, new HashSet<>(), mpaStorage.getMpaById(1L));
    }

    @Test
    public void getUserByIdTest() {
        userStorage.createUser(user1);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1L));
        assertThat(userOptional).hasValueSatisfying(user -> assertThat(user)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("email", "user1@yandex.ru")
                .hasFieldOrPropertyWithValue("login", "userfirst")
                .hasFieldOrPropertyWithValue("name", "User First")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1995, 12, 1)));
    }

    @Test
    public void getAllUsersTest() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);

        Collection<User> userList = userStorage.getAllUsers();
        assertThat(userList).hasSize(2);
        assertThat(userList).contains(user1);
        assertThat(userList).contains(user2);
    }

    @Test
    public void updateUserTest() {
        userStorage.createUser(user1);
        user1.setEmail("seconduser@yandex.ru");
        user1.setName("Second User");
        userStorage.updateUser(user1);

        Optional<User> optionalUser = Optional.ofNullable(userStorage.getUserById(user1.getId()));
        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "seconduser@yandex.ru")
                                .hasFieldOrPropertyWithValue("name", "Second User")
                );
    }

    @Test
    public void addFriendTest() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);

        userStorage.addFriend(user1.getId(), user2.getId());
        assertThat(userStorage.getFriendsList(user1.getId())).hasSize(1);
        assertThat(userStorage.getFriendsList(user1.getId())).contains(user2);
    }

    @Test
    public void deleteFriendTest() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);

        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.deleteFriend(user1.getId(), user2.getId());
        assertThat(userStorage.getFriendsList(user1.getId())).hasSize(0);
        assertThat(userStorage.getFriendsList(user1.getId())).doesNotContain(user2);
    }

    @Test
    public void getFriendsTest() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);
        userStorage.createUser(user4);
        userStorage.addFriend(user1.getId(), user3.getId());
        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.addFriend(user1.getId(), user4.getId());
        Collection<User> friendsList = userStorage.getFriendsList(user1.getId());

        assertThat(friendsList).hasSize(3);
        assertThat(friendsList).contains(user2);
        assertThat(friendsList).contains(user3);
        assertThat(friendsList).contains(user4);
    }

    @Test
    public void getMutualFriendsTest() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);
        userStorage.createUser(user4);
        userStorage.addFriend(user1.getId(), user3.getId());
        userStorage.addFriend(user1.getId(), user4.getId());
        userStorage.addFriend(user2.getId(), user3.getId());
        userStorage.addFriend(user2.getId(), user1.getId());
        userStorage.addFriend(user2.getId(), user4.getId());

        Collection<User> mutualFriends = userStorage.getMutualFriends(user1.getId(), user2.getId());
        assertThat(mutualFriends).hasSize(2);
        assertThat(mutualFriends).contains(user3);
        assertThat(mutualFriends).contains(user4);
        assertThat(mutualFriends).doesNotContain(user1);
    }

    @Test
    public void getFilmByIdTest() {
        filmStorage.createFilm(film1);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(1L));
        assertThat(filmOptional).hasValueSatisfying(film -> assertThat(film)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "First Film")
                .hasFieldOrPropertyWithValue("description", "Description first film")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2000, 5, 16))
                .hasFieldOrPropertyWithValue("duration", 185)
                .hasFieldOrPropertyWithValue("mpa", mpaStorage.getMpaById(2L)));
    }

    @Test
    public void getAllFilmsTest() {
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);

        Collection<Film> filmList = filmStorage.getAllFilms();
        assertThat(filmList).hasSize(2);
        assertThat(filmList).contains(film1);
        assertThat(filmList).contains(film2);
    }

    @Test
    public void updateFilmTest() {
        filmStorage.createFilm(film1);
        film1.setName("New name");
        film1.setDescription("New description");
        filmStorage.updateFilm(film1);

        Optional<Film> optionalUser = Optional.ofNullable(filmStorage.getFilmById(film1.getId()));
        assertThat(optionalUser).isPresent().hasValueSatisfying(film -> assertThat(film)
                .hasFieldOrPropertyWithValue("name", "New name")
                .hasFieldOrPropertyWithValue("description", "New description")
        );
    }

    @Test
    public void likeFilmTest() {
        filmStorage.createFilm(film1);
        userStorage.createUser(user1);

        assertThat(filmStorage.getLikes(film1.getId())).hasSize(0);

        filmStorage.likeFilm(film1.getId(), user1.getId());

        assertThat(filmStorage.getLikes(film1.getId())).hasSize(1);
        assertThat(filmStorage.getLikes(film1.getId())).contains(user1.getId());
    }

    @Test
    public void deleteLikeTest() {
        filmStorage.createFilm(film1);
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        filmStorage.likeFilm(film1.getId(), user1.getId());
        filmStorage.likeFilm(film1.getId(), user2.getId());
        filmStorage.deleteLike(film1.getId(), user1.getId());

        assertThat(filmStorage.getLikes(film1.getId())).hasSize(1);
        assertThat(filmStorage.getLikes(film1.getId())).contains(user2.getId());
    }

    @Test
    public void getLikesTest() {
        filmStorage.createFilm(film1);
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        filmStorage.likeFilm(film1.getId(), user1.getId());
        filmStorage.likeFilm(film1.getId(), user2.getId());

        assertThat(filmStorage.getLikes(film1.getId())).hasSize(2);
        assertThat(filmStorage.getLikes(film1.getId())).contains(user2.getId());
        assertThat(filmStorage.getLikes(film1.getId())).contains(user1.getId());
    }

    @Test
    public void getMostLikedFilms() {
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        filmStorage.likeFilm(film1.getId(), user1.getId());
        filmStorage.likeFilm(film1.getId(), user2.getId());
        filmStorage.likeFilm(film2.getId(), user1.getId());

        assertThat(filmStorage.getMostLikedFilms(3)).hasSize(2);
        assertThat(filmStorage.getMostLikedFilms(3)).contains(film1);
        assertThat(filmStorage.getMostLikedFilms(3)).contains(film2);
    }

    @Test
    public void getAllGenresTest() {
        Genre genre1 = genreStorage.getGenreById(1L);
        Genre genre2 = genreStorage.getGenreById(2L);
        Genre genre3 = genreStorage.getGenreById(3L);
        Genre genre4 = genreStorage.getGenreById(4L);
        Genre genre5 = genreStorage.getGenreById(5L);
        Genre genre6 = genreStorage.getGenreById(6L);

        List<Genre> genreList = genreStorage.getAllGenres();
        assertThat(genreList).hasSize(6);
        assertThat(genreList).contains(genre1, genre2, genre3, genre4, genre5, genre6);
    }

    @Test
    public void getGenreByIdTest() {

        Optional<Genre> mpaOptional = Optional.ofNullable(genreStorage.getGenreById(1L));
        assertThat(mpaOptional).hasValueSatisfying(genre -> assertThat(genre)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Комедия"));
    }

    @Test
    public void getAllMpaTest() {
        Mpa mpa1 = mpaStorage.getMpaById(1L);
        Mpa mpa2 = mpaStorage.getMpaById(2L);
        Mpa mpa3 = mpaStorage.getMpaById(3L);
        Mpa mpa4 = mpaStorage.getMpaById(4L);
        Mpa mpa5 = mpaStorage.getMpaById(5L);

        List<Mpa> mpaList = mpaStorage.getAllMpa();
        assertThat(mpaList).hasSize(5);
        assertThat(mpaList).contains(mpa1, mpa2, mpa3, mpa4, mpa5);
    }

    @Test
    public void getMpaByIdTest() {
        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaStorage.getMpaById(1L));
        assertThat(mpaOptional).hasValueSatisfying(mpa -> assertThat(mpa)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "G"));
    }
}