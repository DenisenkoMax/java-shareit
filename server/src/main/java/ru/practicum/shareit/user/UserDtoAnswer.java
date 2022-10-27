package ru.practicum.shareit.user;

import lombok.Getter;
@Getter
public class UserDtoAnswer {
    Long id;
    private String name;
    private String email;

    public UserDtoAnswer(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
