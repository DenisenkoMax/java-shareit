package ru.practicum.shareit.userTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUserStatus200Test() throws Exception {
        UserDto userDto = new UserDto("user1", "user1@domen.ru");
        User user = new User(1L, "user1", "user1@domen.ru", null, null, null);

        when(service.createUser(userDto))
                .thenReturn(Optional.of(user));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("user1")))
                .andExpect(jsonPath("$.email", is("user1@domen.ru")));
        verify(service, Mockito.times(1)).createUser(userDto);
    }

    @Test
    void createUserErrorEmailStatus400Test() throws Exception {
        UserDto userDto = new UserDto("user1", "1");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserStatus200Test() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto("user1", "user1@domen.ru");
        User user = new User(userId, "user1", "user1@domen.ru", null, null, null);
        when(service.updateUser(userDto, userId))
                .thenReturn(Optional.of(user));
        mockMvc.perform(patch("/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("user1")))
                .andExpect(jsonPath("$.email", is("user1@domen.ru")));
        verify(service, Mockito.times(1)).updateUser(userDto, userId);
    }

    @Test
    public void findUserByIdStatus200Test() throws Exception {
        Long userId = 1L;
        User user = new User(userId, "user", "user@mail.ru", null, null, null);
        when(service.findUserById(userId)).thenReturn(Optional.of(user));
        mockMvc.perform(get("/users/{id}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
        verify(service, Mockito.times(1)).findUserById(userId);
    }

    @Test
    public void findUserErrorIdStatus404() throws Exception {
        Long userId = 1L;
        when(service.findUserById(userId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/users/{id}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, Mockito.times(1)).findUserById(userId);
    }

    @Test
    public void getAllUsersStatus200() throws Exception {
        Long userId = 1L;
        User user = new User(userId, "user", "user@mail.ru", null, null, null);
        when(service.getAllUsers()).thenReturn(List.of(user));
        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(user.getName())))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())));
        verify(service, Mockito.times(1)).getAllUsers();
    }

    @Test
    public void deleteUserByIdStatus200() throws Exception {
        when(service.deleteUserById(1L)).thenReturn(true);
        mockMvc.perform(delete("/users/{id}", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(service, Mockito.times(1)).deleteUserById(1L);
    }
}