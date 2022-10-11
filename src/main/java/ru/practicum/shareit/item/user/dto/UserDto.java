package ru.practicum.shareit.item.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    @Email(regexp = "\\w+@\\w+\\.(ru|com)",
            message = "Email should be valid")
    private String email;

    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
