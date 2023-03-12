create table if not exists MPA
(
    MPA_ID   INTEGER not null auto_increment,
    MPA_NAME VARCHAR_IGNORECASE not null,
    constraint MPA_PK
        primary key (MPA_ID)
);

create table if not exists GENRES
(
    GENRE_ID   INTEGER not null auto_increment,
    GENRE_NAME VARCHAR_IGNORECASE not null,
    constraint GENRES_PK
        primary key (GENRE_ID)
);

create table if not exists FILMS
(
    FILM_ID      INTEGER not null auto_increment,
    NAME         VARCHAR_IGNORECASE not null,
    DESCRIPTION  VARCHAR_IGNORECASE(200),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    MPA_ID       INTEGER not null,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_FK
        foreign key (MPA_ID) references MPA
);

create table if not exists FILM_GENRE
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRE_UK
        unique (FILM_ID, GENRE_ID),
    constraint FILM_GENRE_FK
        foreign key (FILM_ID) references PUBLIC.FILMS
            on update cascade on delete cascade,
    constraint FILM_GENRE_FK_1
        foreign key (GENRE_ID) references PUBLIC.GENRES
            on update cascade on delete cascade
);

create table if not exists USERS
(
    USER_ID  INTEGER not null auto_increment,
    EMAIL    VARCHAR_IGNORECASE not null,
    NAME     VARCHAR_IGNORECASE,
    LOGIN    VARCHAR_IGNORECASE not null,
    BIRTHDAY DATE,
    constraint USERS_PK
        primary key (USER_ID)
);

create table if not exists FRIENDS
(
    USER_ID    INTEGER not null,
    FRIEND_ID  INTEGER not null,
    constraint FRIENDS_UN
        unique (USER_ID, FRIEND_ID),
    constraint FRIENDS_FK
        foreign key (USER_ID) references PUBLIC.USERS
            on update cascade on delete cascade,
    constraint FRIENDS_FK_1
        foreign key (FRIEND_ID) references PUBLIC.USERS
            on update cascade on delete cascade
);

create table if not exists LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_UN
        unique (FILM_ID, USER_ID),
    constraint LIKES_FK
        foreign key (FILM_ID) references PUBLIC.FILMS
            on update cascade on delete cascade,
    constraint LIKES_FK_1
        foreign key (USER_ID) references PUBLIC.USERS
            on update cascade on delete cascade
);
