package ru.practicum.shareit.bookingTests;

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
import ru.practicum.shareit.item.dto.ItemDtoAnswer;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingIntegrationTest {
    private final ItemService itemService;
    private final BookingService bookingService;
    private final EntityManager entityManager;
    private User user;
    private User user2;
    private Item item;
    private Booking booking;

    @BeforeEach
    public void before() {
        user = new User(null, "user", "user@email.ru", null, null, null);
        entityManager.persist(user);
        user2 = new User(null, "user2", "user2@email.ru", null, null, null);
        entityManager.persist(user2);
        item = new Item(null, "Молоток", "кривой", user2, true, null);
        entityManager.persist(item);
        booking = new Booking(null, LocalDateTime.now().minusHours(20L), LocalDateTime.now()
                .minusHours(10L), item, user, BookingStatus.APPROVED);
        entityManager.persist(booking);
    }

    @Test
    public void createBookingAndConfirmTest() throws NotFoundEx, IllegalArgumentEx {
        Comment comment = new Comment(null, "комментарий", item, user2,
                LocalDateTime.now());
        entityManager.persist(comment);
        ItemDtoAnswer itemDtoAnswer = itemService.getItemsByOwner(user2.getId(), 0, 15).get(0);
        assertThat(comment.getText(), equalTo(itemDtoAnswer.getComments()
                .stream().collect(Collectors.toList()).get(0).getText()));
    }

    @Test
    public void getUserBookingsTest() throws NotFoundEx, IllegalArgumentEx {
        assertThat(booking.getId(), equalTo(bookingService.getUserBookings(user.getId(), "PAST", 0, 10)
                .get(0).getId()));
    }

    @Test
    public void getUserItemTest() throws NotFoundEx, IllegalArgumentEx {
        System.out.println(item.getId());
        System.out.println(bookingService.getItemsBookings(user2.getId(), "PAST", 0, 10)
                .get(0).getId());
        assertThat(item.getId(), equalTo(bookingService.getItemsBookings(user2.getId(), "PAST", 0, 10)
                .get(0).getId()));
    }
}
