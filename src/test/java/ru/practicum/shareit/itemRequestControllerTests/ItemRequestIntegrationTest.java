package ru.practicum.shareit.itemRequestControllerTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.item.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestIntegrationTest {
    private final ItemRequestService itemRequestService;
    private final EntityManager em;
    private User user;
    private User user2;
    private Item item;
    private ItemRequest itemRequest;
    private ItemRequest itemRequest2;

    @BeforeEach
    public void before() {
        user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        em.persist(user);
        user2 = new User(null, "name2", "email2@dffd.ru", null,
                null, null);
        em.persist(user2);
        itemRequest = new ItemRequest(null, "text", user, LocalDateTime.now(), null);
        em.persist(itemRequest);
        itemRequest2 = new ItemRequest(null, "text2", user, LocalDateTime.now().plusHours(1L), null);
        em.persist(itemRequest2);
    }

    @Test
    public void createItemRequest() {

        TypedQuery<ItemRequest> query = em.createQuery("Select u from ItemRequest u where u.id = :id",
                ItemRequest.class);
        ItemRequest itemRequestAnswer = query
                .setParameter("id", itemRequest.getId())
                .getSingleResult();
        Assertions.assertEquals(itemRequest.getId(), itemRequestAnswer.getId());
    }

    @Test
    public void findUserOwnerItemRequests() throws NotFoundEx, IllegalArgumentEx {
        Assertions.assertEquals(itemRequest.getDescription(), itemRequestService
                .findUserOwnerItemRequests(user.getId(), 0, 10).get(1).getDescription());
        Assertions.assertEquals(itemRequest2.getDescription(), itemRequestService
                .findUserOwnerItemRequests(user.getId(), 0, 10).get(0).getDescription());
    }

    @Test
    public void findAnotherUsersItemRequests() throws NotFoundEx, IllegalArgumentEx {
        Assertions.assertEquals(itemRequest.getDescription(), itemRequestService
                .findAnotherUsersItemRequests(user2.getId(), 0, 10).get(1).getDescription());
        Assertions.assertEquals(itemRequest2.getDescription(), itemRequestService
                .findAnotherUsersItemRequests(user2.getId(), 0, 10).get(0).getDescription());
    }
}
