package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@DataJpaTest
public class BookingReposytoryJpaTest {
    @Autowired
    private BookingRepositoryJpa bookingRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findBookingsByUserTest() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(null, start, end, item, user, BookingStatus.WAITING);
        entityManager.persist(booking);
        Booking loadedBooking = bookingRepository.findBookingsByUser(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0);
        Assertions.assertEquals(booking.getStart(), loadedBooking.getStart());
        Assertions.assertEquals(booking.getEnd(), loadedBooking.getEnd());
    }

    @Test
    public void findBookingsByUserAndStatusTest() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(null, start, end, item, user, BookingStatus.WAITING);
        entityManager.persist(booking);
        Booking loadedBooking = bookingRepository.findBookingsByUser(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0);
        Assertions.assertEquals(booking.getStart(), loadedBooking.getStart());
        Assertions.assertEquals(booking.getEnd(), loadedBooking.getEnd());
    }

    @Test
    public void findPastBookingsByUserTest() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        LocalDateTime start = LocalDateTime.now().minusDays(15);
        LocalDateTime end = LocalDateTime.now().minusDays(10);
        Booking booking = new Booking(null, start, end, item, user, BookingStatus.WAITING);
        entityManager.persist(booking);
        Booking loadedBooking = bookingRepository.findBookingsByUser(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0);
        Assertions.assertEquals(booking.getStart(), loadedBooking.getStart());
        Assertions.assertEquals(booking.getEnd(), loadedBooking.getEnd());
    }

    @Test
    public void findFutureBookingsByUserTest() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(null, start, end, item, user, BookingStatus.WAITING);
        entityManager.persist(booking);
        Booking loadedBooking = bookingRepository.findBookingsByUser(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0);
        Assertions.assertEquals(booking.getStart(), loadedBooking.getStart());
        Assertions.assertEquals(booking.getEnd(), loadedBooking.getEnd());
    }

    @Test
    public void findCurrentBookingsByUserTest() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(null, start, end, item, user, BookingStatus.WAITING);
        entityManager.persist(booking);
        Assertions.assertEquals(booking.getStart(), bookingRepository.findBookingsByUser(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0).getStart());
        Assertions.assertEquals(booking.getEnd(), bookingRepository.findCurrentBookingsByUser(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0).getEnd());
    }

    @Test
    public void findItemBookingsByUserTest() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        User user2 = new User(null, "name2", "email2@dffd.ru", null,
                null, null);
        entityManager.persist(user2);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        Item item2 = new Item(null, "пила", "пила", user2, true, null);
        entityManager.persist(item2);
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(null, start, end, item, user2, BookingStatus.WAITING);
        entityManager.persist(booking);
        Booking booking2 = new Booking(null, start, end, item2, user, BookingStatus.WAITING);
        entityManager.persist(booking2);
        Assertions.assertEquals(booking.getStart(), bookingRepository.findBookingsByUser(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0).getStart());
        Assertions.assertEquals(booking.getEnd(), bookingRepository.findItemBookingsByUser(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0).getEnd());
        Assertions.assertEquals(booking2.getStart(), bookingRepository.findBookingsByUser(user2.getId(),
                PageRequest.of(0, 10)).getContent().get(0).getStart());
        Assertions.assertEquals(booking2.getEnd(), bookingRepository.findItemBookingsByUser(user2.getId(),
                PageRequest.of(0, 10)).getContent().get(0).getEnd());
    }

    @Test
    public void findItemBookingsByUserAndStatusTest() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        User user2 = new User(null, "name2", "email2@dffd.ru", null,
                null, null);
        entityManager.persist(user2);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(null, start, end, item, user2, BookingStatus.WAITING);
        entityManager.persist(booking);
        Booking loadedBookingFindItemBookingsByUserAndStatus = bookingRepository
                .findItemBookingsByUserAndStatus(user.getId(),
                        BookingStatus.WAITING, PageRequest.of(0, 10)).getContent().get(0);
        Assertions.assertEquals(booking.getStart(), loadedBookingFindItemBookingsByUserAndStatus.getStart());
        Assertions.assertEquals(booking.getEnd(), loadedBookingFindItemBookingsByUserAndStatus.getEnd());
    }

    @Test
    public void findPastItemBookingsByUserTest() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        User user2 = new User(null, "name2", "email2@dffd.ru", null,
                null, null);
        entityManager.persist(user2);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        LocalDateTime start = LocalDateTime.now().minusDays(10);
        LocalDateTime end = LocalDateTime.now().minusDays(3);
        Booking booking = new Booking(null, start, end, item, user2, BookingStatus.WAITING);
        entityManager.persist(booking);
        Booking loadedBooking = bookingRepository.findPastItemBookingsByUser(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0);
        Assertions.assertEquals(booking.getStart(), loadedBooking.getStart());
        Assertions.assertEquals(booking.getEnd(), loadedBooking.getEnd());
    }

    @Test
    public void findFutureItemBookingsByUserTest() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        User user2 = new User(null, "name2", "email2@dffd.ru", null,
                null, null);
        entityManager.persist(user2);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        Booking booking = new Booking(null, start, end, item, user2, BookingStatus.WAITING);
        entityManager.persist(booking);
        Booking loadedBooking = bookingRepository.findFutureItemBookingsByUser(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0);
        Assertions.assertEquals(booking.getStart(), loadedBooking.getStart());
        Assertions.assertEquals(booking.getEnd(), loadedBooking.getEnd());
    }

    @Test
    public void findCurrentItemBookingsByUserTest() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        User user2 = new User(null, "name2", "email2@dffd.ru", null,
                null, null);
        entityManager.persist(user2);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        Booking booking = new Booking(null, start, end, item, user2, BookingStatus.WAITING);
        entityManager.persist(booking);
        Booking loadedBooking = bookingRepository.findCurrentItemBookingsByUser(user.getId(),
                PageRequest.of(0, 10)).getContent().get(0);
        Assertions.assertEquals(booking.getStart(), loadedBooking.getStart());
        Assertions.assertEquals(booking.getEnd(), loadedBooking.getEnd());
    }

    @Test
    public void findBookingDatesTest() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        User user2 = new User(null, "name2", "email2@dffd.ru", null,
                null, null);
        entityManager.persist(user2);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        Booking booking = new Booking(null, start, end, item, user2, BookingStatus.WAITING);
        entityManager.persist(booking);
        Assertions.assertTrue(bookingRepository.findBookingDates(item.getId(),
                start.plusHours(1), end.minusHours(1)));
    }

    @Test
    public void getLastItemBookings() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        User user2 = new User(null, "name2", "email2@dffd.ru", null,
                null, null);
        entityManager.persist(user2);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        Booking booking = new Booking(null, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(1),
                item, user2, BookingStatus.WAITING);
        entityManager.persist(booking);
        Booking booking2 = new Booking(null, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(5),
                item, user2, BookingStatus.WAITING);
        entityManager.persist(booking2);
        Booking loadedBooking = bookingRepository.getLastItemBookings(item.getId(), user.getId());
        Assertions.assertEquals(booking.getStart(), loadedBooking.getStart());
        Assertions.assertEquals(booking.getEnd(), loadedBooking.getEnd());
    }

    @Test
    public void getNextItemBookings() {
        User user = new User(null, "name", "email@dffd.ru", null,
                null, null);
        entityManager.persist(user);
        User user2 = new User(null, "name2", "email2@dffd.ru", null,
                null, null);
        entityManager.persist(user2);
        Item item = new Item(null, "Молоток", "пластиковый", user, true, null);
        entityManager.persist(item);
        Booking booking = new Booking(null, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(1),
                item, user2, BookingStatus.WAITING);
        entityManager.persist(booking);
        Booking booking2 = new Booking(null, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(5),
                item, user2, BookingStatus.WAITING);
        entityManager.persist(booking2);
        Booking loadedBooking = bookingRepository.getNextItemBookings(item.getId(), user.getId());
        Assertions.assertEquals(booking2.getStart(), loadedBooking.getStart());
        Assertions.assertEquals(booking2.getEnd(), loadedBooking.getEnd());
    }
}
