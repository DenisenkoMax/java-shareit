package ru.practicum.shareit.userTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserIntegrationTests {
    private final UserService service;
    private final EntityManager em;

    @Test
    public void CreateUserTest() {
        UserDto userDto = new UserDto("user", "user@email.ru");
        service.createUser(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query
                .setParameter("email", userDto.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    public void UpdateUserTest() throws NotFoundEx {
        UserDto userDto = new UserDto("user", "user@mail.ru");
        service.createUser(userDto);
        userDto.setName("newuser");
        userDto.setEmail("updateuser@mail.ru");
        service.updateUser(userDto, 1L);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query
                .setParameter("email", userDto.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    public void GetAllUsers() throws NotFoundEx {
        UserDto userDto = new UserDto("user", "user@mail.ru");
        service.createUser(userDto);
        UserDto userDto1 = new UserDto("user2", "user2@mail.ru");
        service.createUser(userDto1);

        List<User> result = service.getAllUsers();

        assertThat(result.get(0).getId(), notNullValue());
        assertThat(result.get(0).getName(), equalTo(userDto.getName()));
        assertThat(result.get(0).getEmail(), equalTo(userDto.getEmail()));

        assertThat(result.get(1).getId(), notNullValue());
        assertThat(result.get(1).getName(), equalTo(userDto1.getName()));
        assertThat(result.get(1).getEmail(), equalTo(userDto1.getEmail()));
    }
}
