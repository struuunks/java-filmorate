
![filmorate_diagram](https://user-images.githubusercontent.com/113509716/224560605-65bc445c-a5c2-4129-b824-a1f31d498275.png)

Таблица FILMS:

  FILM_ID - айди фильма, первичный ключ;
  NAME - название фильма;
  DESCRIPTION - описание фильма;
  RELEASE_DATE - дата выхода фильма;
  DURATION - продолжительность фильма;
  MPA_ID - айди рейтинга, внешний ключ отсылающий к таблице MPA;

Таблица MPA:

  MPA_ID - айди рейтинга, первичный ключ;
  MPA_NAME - название рейтинга;

Таблица FILM_GENRES:

  FILM_ID - айди фильма, уникальный ключ отсылающий к таблице FILMS;
  GENRE_ID - айди жанра, уникальный ключ отсылающий к таблице GENRES;

Таблица GENRES:

  GENRE_ID - айди жанра, первичный ключ;
  GENRE_NAME - название жанра;

Таблица USERS:

  USER_ID - айди пользователя, первичный ключ;
  EMAIL - электронная почта;
  NAME - имя пользователя;
  LOGIN - логин пользователя;
  BIRTHDAY - дата рождения пользователя;

Таблица FRIENDS:

  USER_ID - айди пользователя, уникальный ключ отсылающий к таблице USERS;
  FRIEND_ID - айди пользователя, уникальный ключ отсылающий к таблице USERS;

Таблица LIKES:

  FILM_ID - айди фильма, уникальный ключ отсылающий к таблице FILMS;
  USER_ID - айди пользователя, уникальный ключ отсылающий к таблице USERS;
   
Примеры запросов:

  Получение списка 10 популярных фильмов:
    select f.* from films f
    left join likes l on f.film_id = l.film_id
    group by  f.film_id
    order by count(l.user_id) desc
    limit 10;
    
    
  Получение списка всех фильмов:
    select * from films;
  
  Получение спика всех пользователей:
    select * from users;
    
  Получение списка друзей пользователя:
    select u.user_id, u.email, u.name, u.login, u.birthday
    from friends as f left join users as u
    on f.friend_id = u.user_id where f.user_id = ?
    order by u.user_id;
