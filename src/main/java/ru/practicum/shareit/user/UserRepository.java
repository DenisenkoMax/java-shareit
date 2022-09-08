package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.ValidationException;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    User create(User user) throws ValidationException;

    Optional<User> findUserById(long id);

    boolean deleteUserById(long id);

    Optional<User> update(User user) throws ValidationException;
}