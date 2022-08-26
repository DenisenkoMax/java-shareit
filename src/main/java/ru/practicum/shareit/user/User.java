package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

/**
 * // TODO .
 */
@Data
@Builder
public class User {
    private long id;
    private String name;
    private String email;

    public User(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
