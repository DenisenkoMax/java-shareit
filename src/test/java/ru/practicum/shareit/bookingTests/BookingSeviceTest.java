package ru.practicum.shareit.bookingTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import ru.practicum.shareit.item.user.UserMapper;
import ru.practicum.shareit.item.user.UserRepositoryJpa;
import ru.practicum.shareit.item.user.model.User;
import ru.practicum.shareit.validation.Validation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@Transactional
@RequiredArgsConstructor

public class BookingSeviceTest {
    @Mock
    private BookingRepositoryJpa bookingRepositoryJpa;
    @Mock
    private UserRepositoryJpa userRepositoryJpa;
    @Mock
    private ItemRepositoryJpa itemRepositoryJpa;
    @Mock
    private Validation validation;
    @Mock
    private BookingServiceImpl bookingService;

    @BeforeEach
    public void before() {
        bookingRepositoryJpa = Mockito.mock(BookingRepositoryJpa.class);
        userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
        itemRepositoryJpa = Mockito.mock(ItemRepositoryJpa.class);
        validation = Mockito.mock(Validation.class);
        bookingService = new BookingServiceImpl(bookingRepositoryJpa, userRepositoryJpa, itemRepositoryJpa, validation);
    }



    @Test
    public void createBooking() throws NotFoundEx, IllegalArgumentEx {
        Long userId = 1L;
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.WAITING);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validateItem(anyLong());
        Mockito.doNothing().when(validation).validateItemAvailable(anyLong());
        Mockito.doNothing().when(validation).validateBookingDate(any());
        when(bookingRepositoryJpa.findBookingDates(anyLong(), any(), any())).thenReturn(false);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.doNothing().when(validation).validateBookerIsOwner(any(), anyLong());
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertEquals(item.getId(), bookingService.create(bookingDto, userId)
                .getItemId());
    }

    @Test
    public void createBookingWrongUser() throws NotFoundEx, IllegalArgumentEx {
        Long userId = 1L;
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.WAITING);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        Mockito.doThrow(new NotFoundEx("")).when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validateItem(anyLong());
        Mockito.doNothing().when(validation).validateItemAvailable(anyLong());
        Mockito.doNothing().when(validation).validateBookingDate(any());
        when(bookingRepositoryJpa.findBookingDates(anyLong(), any(), any())).thenReturn(false);
        when(userRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(item));
        Mockito.doNothing().when(validation).validateBookerIsOwner(any(), anyLong());
        when(bookingRepositoryJpa.save(any())).thenReturn(booking);
        Assertions.assertThrows(NotFoundEx.class,
                () -> bookingService.create(bookingDto, userId));
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
        Mockito.doNothing().when(validation).validateBooking(anyLong());
        Mockito.doNothing().when(validation).validateUser(anyLong());
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(booking));
        Mockito.doNothing().when(validation).validateItemOwner(any(), anyLong());
        Assertions.assertEquals(bookingDtoAnswer.getStatus(), bookingService.confirmBookingRequest(1L, 1L, true)
                .getStatus());
    }

    @Test
    public void getBookingByIdTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        BookingDtoAnswer bookingDtoAnswer = new BookingDtoAnswer(1L, start, end,
                ItemMapper.toItemDto(item), UserMapper.toUserDtoAnswer(user), BookingStatus.APPROVED);
        Mockito.doNothing().when(validation).validateBooking(anyLong());
        Mockito.doNothing().when(validation).validateUser(anyLong());
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(booking));
        Mockito.doNothing().when(validation).validateBookerIsOwner(item, 1L);
        Assertions.assertEquals(bookingDtoAnswer.getStatus(), bookingService.confirmBookingRequest(1L, 1L, true)
                .getStatus());
    }

    @Test
    public void getBookingByIdWrongUserTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        Mockito.doNothing().when(validation).validateBooking(anyLong());
        Mockito.doThrow(new NotFoundEx("")).when(validation).validateUser(anyLong());
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(booking));
        Mockito.doNothing().when(validation).validateBookerIsOwner(item, 1L);
        Assertions.assertThrows(NotFoundEx.class,
                () -> bookingService.confirmBookingRequest(1L, 1L, true));
    }

    @Test
    public void getBookingByIdWrongBookingTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        Mockito.doThrow(new NotFoundEx("")).when(validation).validateBooking(anyLong());
        Mockito.doNothing().when(validation).validateUser(anyLong());
        when(bookingRepositoryJpa.findById(anyLong())).thenReturn(Optional.of(booking));
        Mockito.doNothing().when(validation).validateBookerIsOwner(item, 1L);
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
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
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
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
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
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
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
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
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
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
        when(bookingRepositoryJpa.findCurrentItemBookingsByUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Assertions.assertEquals(1L, bookingService.getItemsBookings(1L, "CURRENT", 0, 10)
                .get(0).getId());
    }

    @Test
    public void findItemBookingsErrorStatusItemBookingsByUserTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Booking booking = new Booking(1L, start, end, item, user, BookingStatus.WAITING);
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
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
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
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
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
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
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
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
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
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
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
        when(bookingRepositoryJpa.findCurrentBookingsByUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Assertions.assertEquals(1L, bookingService.getUserBookings(1L, "CURRENT", 0, 10)
                .get(0).getId());
    }

    @Test
    public void findUserBookingsErrorStatusItemBookingsByUserTest() throws NotFoundEx, IllegalArgumentEx {
        User user = new User(1L, "name", "email@dffd.ru", null,
                null, null);
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Молоток", "пластиковый", user, true, itemRequest);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(10);
        Mockito.doNothing().when(validation).validateUser(anyLong());
        Mockito.doNothing().when(validation).validatePagination(anyInt(), anyInt());
        Assertions.assertThrows(IllegalArgumentEx.class,
                () -> bookingService.getItemsBookings(1L, "CUR", 0, 10));
    }
}