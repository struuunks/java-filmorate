package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class Film {
     private Long id;
     private String name;
     private String description;
     private LocalDate releaseDate;
     private Integer duration;
}
