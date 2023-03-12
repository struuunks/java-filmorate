package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Film {
     private Long id;

     @NonNull
     private String name;

     @Size(max = 200)
     private String description;

     @NonNull
     private LocalDate releaseDate;

     @Positive
     private Integer duration;

     private Set<Genre> genres;

     @NonNull
     private Mpa mpa;
     @JsonIgnore
     private final Set<Long> likes = new HashSet<>();
}
