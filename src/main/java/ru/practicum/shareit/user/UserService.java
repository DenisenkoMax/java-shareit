package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

interface UserService {
    List<User> getAllUsers();

    Optional<User> createUser(UserDto userDto) throws ValidationException;

    Optional<User> findUserById(long id);

    Optional<User> updateUser(UserDto userDto, Long userId) throws ValidationException;

    boolean deleteUserById(long id);


}
