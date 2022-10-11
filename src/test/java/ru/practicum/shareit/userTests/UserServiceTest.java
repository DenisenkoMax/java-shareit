package ru.practicum.shareit.userTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.user.UserMapper;
import ru.practicum.shareit.item.user.UserRepositoryJpa;
import ru.practicum.shareit.item.user.UserService;
import ru.practicum.shareit.item.user.UserServiceImpl;
import ru.practicum.shareit.item.user.dto.UserDto;
import ru.practicum.shareit.item.user.model.User;
import ru.practicum.shareit.validation.Validation;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
@RequiredArgsConstructor
public class UserServiceTest {
    @Mock
    private UserRepositoryJpa userRepositoryJpa;
    @Mock
    private Validation validation;
    private UserService userService;

    @BeforeEach
    public void before() {
        userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
        validation = Mockito.mock(Validation.class);
        userService = new UserServiceImpl(userRepositoryJpa, validation);
    }

    @Test
    public void createUserTest() {
        User user = new User(1L, "name", "email@mail.nmmk", null, null, null);
        UserDto userDto = new UserDto("name", "email");
        when(userRepositoryJpa.save(UserMapper.toUser(userDto))).thenReturn(user);
        Assertions.assertEquals("name", userService.createUser(userDto).get().getName());
    }

    @Test
    public void updateUserTest() throws NotFoundEx {
        User user = new User(1L, "name", "email@mail.nmmk", null, null, null);
        UserDto userDto = new UserDto("name", "email");
        when(userRepositoryJpa.save(any())).thenReturn(user);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Assertions.assertEquals("name", userService.updateUser(userDto, 1L).get().getName());
    }

    @Test
    public void updateUserWrongUserTest() throws NotFoundEx {
        User user = new User(1L, "name", "email@mail.nmmk", null, null, null);
        UserDto userDto = new UserDto("name", "email");
        when(userRepositoryJpa.save(any())).thenReturn(user);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        Mockito.doThrow(new NotFoundEx("user not found")).when(validation).validateUser(anyLong());
        Assertions.assertThrows(NotFoundEx.class,
                () -> userService.updateUser(userDto, 1L).get().getName());
    }

    @Test
    public void getAllUsersTest() throws NotFoundEx {
        User user = new User(1L, "name", "email@mail.nmmk", null, null, null);
        when(userRepositoryJpa.findAll()).thenReturn(List.of(user));
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Assertions.assertEquals("name", userService.getAllUsers().get(0).getName());
    }

    @Test
    public void findByIdTest() throws NotFoundEx {
        User user = new User(1L, "name", "email@mail.nmmk", null, null, null);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Assertions.assertEquals("name", userService.findUserById(1L).get().getName());
    }

    @Test
    public void deleteByIdTest() throws NotFoundEx {
        Mockito.doNothing().when(userRepositoryJpa).deleteById(anyLong());
        userService.deleteUserById(1L);
        verify(userRepositoryJpa, Mockito.times(1)).deleteById(1L);
    }
}
