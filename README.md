# java-filmorate
Схема базы данных.
https://dbdiagram.io/d/63f8d704296d97641d838b9e

Таблицы user и film содержат уникальные данные ключом к которым является id. Таблица friends содержит парные значения id пользователей которые являются ключами и поле friendship указывающее на статус дружбы - подтвержденная или неподтвержденная. Таблица likes содержит парные значения user_id и film_id являющиеся ключами. Таблица film_genre содержит парные значения film_id и genre_id являющиеся ключами, они указывают на id жанров которым принадлежит фильм. Таблица genre содержит уникальные ключи отсылающие к названию жанров. Таблица mpa содержит уникальные ключи mpa_id которые указывают на название возрастного рейтинга и его описание.


![user (1)](https://user-images.githubusercontent.com/113509716/221377796-c6ce167c-3a4d-47b7-887e-1c1619eee439.png)
