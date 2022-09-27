package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.Validation;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepositoryJpa repository;
    private final Validation validation;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public Optional<User> createUser(UserDto userDto) {
        return Optional.ofNullable(repository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public Optional<User> findUserById(long id) throws NotFoundEx {
        validation.validateUser(id);
        return repository.findById(id);
    }

    @Override
    public Optional<User> updateUser(UserDto userDto, Long userId) throws NotFoundEx {
        User user = findUserById(userId).get();
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        return Optional.ofNullable(repository.save(user));
    }

    @Override
    public boolean deleteUserById(long id) {
        repository.deleteById(id);
        return true;
    }
}
