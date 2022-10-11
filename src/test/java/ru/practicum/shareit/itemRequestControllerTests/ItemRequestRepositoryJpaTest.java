package ru.practicum.shareit.itemRequestControllerTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.*;
import ru.practicum.shareit.requests.ItemRequestRepositoryJpa;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.item.user.model.User;

import java.time.LocalDateTime;

@DataJpaTest
public class ItemRequestRepositoryJpaTest {
    @Autowired
    private ItemRequestRepositoryJpa itemRequestRepositoryJpa;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void findItemRequestsByUserTest() {
        entityManager.clear();
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        ItemRequest itemRequest = new ItemRequest(null, "text", user, LocalDateTime.now(), null);
        entityManager.persist(itemRequest);
        Assertions.assertEquals("text", itemRequestRepositoryJpa.findItemRequestsByUser(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0).getDescription());
    }

    @Test
    public void findItemRequestsByAnotherUsersTest() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        User user2 = new User(null, "name2", "email2@dffd.ru", null,
                null, null);
        entityManager.persist(user2);
        ItemRequest itemRequest = new ItemRequest(null, "text", user2, LocalDateTime.now(), null);
        entityManager.persist(itemRequest);
        Assertions.assertEquals("text", itemRequestRepositoryJpa.findItemRequestsByAnotherUsers(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0).getDescription());
    }
}
