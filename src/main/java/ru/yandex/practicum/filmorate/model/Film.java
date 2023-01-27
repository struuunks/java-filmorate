package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class Film {
     private int id;
     private String name;
     private String description;
     private LocalDate releaseDate;
     private Integer duration;
}
