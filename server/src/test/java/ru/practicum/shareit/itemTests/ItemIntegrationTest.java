package ru.practicum.shareit.itemTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemDtoAnswer;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class ItemIntegrationTest {
    private final ItemService itemService;
    private final BookingService bookingService;
    private final EntityManager em;
    private User user;
    private User user2;
    private Item item;
    private ItemRequest itemRequest;
    private ItemRequest itemRequest2;


    @BeforeEach
    public void beforeEach() {
        user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        em.persist(user);
        user2 = new User(null, "name2", "email2@dffd.ru", null,
                null, null);
        em.persist(user2);
        item = new Item(null, "Молоток", "кривой", user, true, null);
        em.persist(item);
        itemRequest = new ItemRequest(null, "text", user, LocalDateTime.now(), null);
        em.persist(itemRequest);
        itemRequest2 = new ItemRequest(null, "text2", user, LocalDateTime.now(), null);
        em.persist(itemRequest2);
    }

    @Test
    public void createItemTest() {
        TypedQuery<Item> query = em.createQuery("Select u from Item u where u.name = :name", Item.class);
        Item item2 = query
                .setParameter("name", item.getName())
                .getSingleResult();
        assertThat(item2.getId(), notNullValue());
        assertThat(item2.getName(), equalTo(item.getName()));
        assertThat(item2.getDescription(), equalTo(item.getDescription()));
    }

    @Test
    public void findByOwner() throws NotFoundEx, IllegalArgumentEx {
        ItemDtoAnswer itemDtoAnswer = itemService.getItemsByOwner(user.getId(), 0, 15).get(0);
        assertThat(itemDtoAnswer.getId(), notNullValue());
        assertThat(itemDtoAnswer.getName(), equalTo(item.getName()));
        assertThat(itemDtoAnswer.getDescription(), equalTo(item.getDescription()));
    }

    @Test
    public void searchTest() throws IllegalArgumentEx {
        ItemDto itemDtoAnswer = itemService.search("Молот", 0, 10).get(0);
        assertThat(itemDtoAnswer.getId(), notNullValue());
        assertThat(itemDtoAnswer.getName(), equalTo(item.getName()));
        assertThat(itemDtoAnswer.getDescription(), equalTo(item.getDescription()));
    }

    @Test
    public void createCommentTest() throws NotFoundEx, IllegalArgumentEx {
        Booking booking = new Booking(null, LocalDateTime.now().minusHours(2L), LocalDateTime.now()
                .minusHours(1L), item, user2, BookingStatus.WAITING);
        em.persist(booking);
        bookingService.confirmBookingRequest(booking.getId(), user.getId(), true);
        CommentDto commentDto = new CommentDto(1L, "комментарий", 1L, "name",
                LocalDateTime.now());
        itemService.createComment(user2.getId(), item.getId(), commentDto);
        ItemDtoAnswer itemDtoAnswer = itemService.getItemsByOwner(user.getId(), 0, 15).get(0);
        assertThat(commentDto.getText(), equalTo(itemDtoAnswer.getComments()
                .stream().collect(Collectors.toList()).get(0).getText()));
    }
}
