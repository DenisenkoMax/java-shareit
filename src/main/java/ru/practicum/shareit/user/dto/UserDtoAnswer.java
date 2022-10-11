package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDtoAnswer {
    Long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    @Email(regexp = "\\w+@\\w+\\.(ru|com)",
            message = "Email should be valid")
    private String email;

    public UserDtoAnswer(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
