package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;

    @NonNull
    @Email
    private String email;

    @NotBlank
    private String login;

    private String name;

    @NonNull
    @PastOrPresent
    private LocalDate birthday;

    @JsonIgnore
    private final Set<Long> friends = new HashSet<>();
}
