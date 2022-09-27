package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

interface UserService {
    List<User> getAllUsers();

    Optional<User> createUser(UserDto userDto);

    Optional<User> findUserById(long id) throws NotFoundEx;

    Optional<User> updateUser(UserDto userDto, Long userId) throws NotFoundEx;

    boolean deleteUserById(long id);


}
