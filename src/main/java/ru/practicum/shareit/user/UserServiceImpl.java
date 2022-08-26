package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(@Qualifier("userRepositoryImpl") UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public Optional<User> createUser(UserDto userDto) throws ValidationException {
        if (validateUserDto(userDto) && validateEmail(userDto)) {
            return Optional.ofNullable(repository.create(UserMapper.toUser(userDto)));
        } else return Optional.empty();
    }

    @Override
    public Optional<User> findUserById(long id) {
        return repository.findUserById(id);
    }

    @Override
    public Optional<User> updateUser(UserDto userDto, Long userId) throws ValidationException {
        User user;
        user = UserMapper.toUser(userDto);
        user.setId(userId);
        if (userDto.getEmail() != null) {
            if (!validateEmail(userDto)) {
                return Optional.empty();
            }
        }
        return repository.update(user);
    }

    @Override
    public boolean deleteUserById(long id) {
        return repository.deleteUserById(id);
    }

    private boolean validateUserDto(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()
                || userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            return false;
        } else return true;
    }

    private boolean validateEmail(UserDto userDto) {
        if (!(userDto.getEmail().contains("@"))
        ) {
            return false;
        } else return true;
    }
}
