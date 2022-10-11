package ru.practicum.shareit.item.user;

import ru.practicum.shareit.item.user.dto.UserDto;
import ru.practicum.shareit.item.user.dto.UserDtoAnswer;
import ru.practicum.shareit.item.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getName(),
                user.getEmail()
        );
    }

    public static UserDtoAnswer toUserDtoAnswer(User user) {
        return new UserDtoAnswer(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                0L,
                userDto.getName(),
                userDto.getEmail(),
                null,
                null,
                null
        );
    }
}
