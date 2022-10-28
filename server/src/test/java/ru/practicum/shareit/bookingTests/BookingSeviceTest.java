package ru.practicum.shareit.bookingTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepositoryJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.Validation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Transactional
@RequiredArgsConstructor

public class BookingSeviceTest {
    @Mock
    private BookingRepositoryJpa bookingRepositoryJpa = Mockito.mock(BookingRepositoryJpa.class);
    @Mock
    private UserRepositoryJpa userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
    @Mock
    private ItemRepositoryJpa itemRepositoryJpa = Mockito.mock(ItemRepositoryJpa.class);
    private final Validation validation = new Validation(userRepositoryJpa, itemRepositoryJpa, bookingRepositoryJpa);
    private final BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepositoryJpa,
            userRepositoryJpa, itemRepositoryJpa, validation);

    @Test
    public void createBooking() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.WAITING);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findBookingDates(anyLong(), any(), any())).thenReturn(false);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertEquals(item.getId(), bookingService.create(bookingDto, 2L)
                .getItem().getId());
    }

    @Test
    public void createBookingItemDatesIsBusy() {
        Long userId = 1L;
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findBookingDates(anyLong(), any(), any())).thenReturn(false);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.findBookingDates(anyLong(), any(), any())).thenReturn(true);
        Assertions.assertThrows(NotFoundEx.class,
                () -> bookingService.create(bookingDto, userId));
    }

    @Test
    public void createBookingWrongUser() {
        Long userId = 1L;
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.WAITING);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findBookingDates(anyLong(), any(), any())).thenReturn(false);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertThrows(NotFoundEx.class,
                () -> bookingService.create(bookingDto, userId));
    }

    @Test
    public void createBookingUserIsNotOwner() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.WAITING);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findBookingDates(anyLong(), any(), any())).thenReturn(false);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertThrows(NotFoundEx.class,
                () -> bookingService.create(bookingDto, 1L));
    }

    @Test
    public void createBookingNotAvailable() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, false, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.WAITING);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findBookingDates(anyLong(), any(), any())).thenReturn(false);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertThrows(IllegalArgumentEx.class,
                () -> bookingService.create(bookingDto, 2L));
    }

    @Test
    public void confirmBookingRequestTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        BookingDtoAnswer bookingDtoAnswer = new BookingDtoAnswer(1L, start, end,
                ItemMapper.toItemDto(item), UserMapper.toUserDtoAnswer(user), BookingStatus.APPROVED);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertEquals(bookingDtoAnswer.getStatus(), bookingService.confirmBookingRequest(1L, 1L, true)
                .getStatus());
    }

    @Test
    public void confirmBookingRequestSetFalseTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        BookingDtoAnswer bookingDtoAnswer = new BookingDtoAnswer(1L, start, end,
                ItemMapper.toItemDto(item), UserMapper.toUserDtoAnswer(user), BookingStatus.REJECTED);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertEquals(bookingDtoAnswer.getStatus(), bookingService.confirmBookingRequest(1L,
                1L, false).getStatus());
    }

    @Test
    public void confirmBookingRequestAlredyAvailableTest() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.APPROVED);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertThrows(IllegalArgumentEx.class,
                () -> bookingService.confirmBookingRequest(1L, 1L, true));
    }

    @Test
    public void confirmBookingRequestUserIsNotItemsOwnerTest() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertThrows(NotFoundEx.class,
                () -> bookingService.confirmBookingRequest(1L, 2L, true));
    }

    @Test
    public void getBookingByIdTest() throws NotFoundEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.APPROVED);
        BookingDtoAnswer bookingDtoAnswer = new BookingDtoAnswer(1L, start, end,
                ItemMapper.toItemDto(item), UserMapper.toUserDtoAnswer(user), BookingStatus.APPROVED);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertEquals(bookingDtoAnswer.getStatus(), bookingService.getBookingById(1L, 1L)
                .getStatus());
    }

    @Test
    public void getBookingByIdUserIsNotBookerOrOwnerTest() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        User user2 = new User(2L, "name2", "email2@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user2, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user2, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertThrows(NotFoundEx.class,
                () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    public void getBookingByIdWrongBookingTest() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertThrows(NotFoundEx.class,
                () -> bookingService.confirmBookingRequest(1L, 1L, true));
    }

    @Test
    public void getItemsBookingsAllTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        when(bookingRepositoryJpa.findItemBookingsByUser(anyLong(), any())).thenReturn(new PageImpl<>(List.of(booking)));
        Assertions.assertEquals(1L, bookingService.getItemsBookings(1L, "ALL", 0, 10)
                .get(0).getId());
    }

    @Test
    public void findItemBookingsByUserAndStatusTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        when(bookingRepositoryJpa.findItemBookingsByUserAndStatus(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Assertions.assertEquals(1L, bookingService.getItemsBookings(1L, "WAITING", 0, 10)
                .get(0).getId());
    }

    @Test
    public void findItemBookingsPastItemBookingsByUserTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        when(bookingRepositoryJpa.findPastItemBookingsByUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Assertions.assertEquals(1L, bookingService.getItemsBookings(1L, "PAST", 0, 10)
                .get(0).getId());
    }

    @Test
    public void findItemBookingsFutureItemBookingsByUserTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        when(bookingRepositoryJpa.findFutureItemBookingsByUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Assertions.assertEquals(1L, bookingService.getItemsBookings(1L, "FUTURE", 0, 10)
                .get(0).getId());
    }

    @Test
    public void findItemBookingsCurrentItemBookingsByUserTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        when(bookingRepositoryJpa.findCurrentItemBookingsByUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Assertions.assertEquals(1L, bookingService.getItemsBookings(1L, "CURRENT", 0, 10)
                .get(0).getId());
    }

    @Test
    public void findItemBookingsErrorStatusItemBookingsByUserTest() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertThrows(IllegalArgumentEx.class,
                () -> bookingService.getItemsBookings(1L, "CUR", 0, 10));
    }

    @Test
    public void getUserBookingsAllTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        when(bookingRepositoryJpa.findBookingsByUser(anyLong(), any())).thenReturn(new PageImpl<>(List.of(booking)));
        Assertions.assertEquals(1L, bookingService.getUserBookings(1L, "ALL", 0, 10)
                .get(0).getId());
    }

    @Test
    public void findUserBookingsByUserAndStatusTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        when(bookingRepositoryJpa.findBookingsByUserAndStatus(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Assertions.assertEquals(1L, bookingService.getUserBookings(1L, "WAITING", 0, 10)
                .get(0).getId());
    }

    @Test
    public void findUserBookingsPastItemBookingsByUserTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        when(bookingRepositoryJpa.findPastBookingsByUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Assertions.assertEquals(1L, bookingService.getUserBookings(1L, "PAST", 0, 10)
                .get(0).getId());
    }

    @Test
    public void findUserBookingsFutureItemBookingsByUserTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        when(bookingRepositoryJpa.findFutureBookingsByUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Assertions.assertEquals(1L, bookingService.getUserBookings(1L, "FUTURE", 0, 10)
                .get(0).getId());
    }

    @Test
    public void findUserBookingsCurrentItemBookingsByUserTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        when(bookingRepositoryJpa.findCurrentBookingsByUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Assertions.assertEquals(1L, bookingService.getUserBookings(1L, "CURRENT", 0, 10)
                .get(0).getId());
    }

    @Test
    public void findUserBookingsErrorStatusItemBookingsByUserTest() {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        Assertions.assertThrows(IllegalArgumentEx.class,
                () -> bookingService.getItemsBookings(1L, "CUR", 0, 10));
    }
}