package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Genre {
    private Long id;

    private String name;
}
