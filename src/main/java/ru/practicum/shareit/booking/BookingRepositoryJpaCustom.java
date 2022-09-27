package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepositoryJpaCustom {
    List<Booking> findBookingsByUser(Long userId);

    List<Booking> findBookingsByUserAndStatus(Long userId, BookingStatus status);

    List<Booking> findPastBookingsByUser(Long userId);

    List<Booking> findFutureBookingsByUser(Long userId);

    List<Booking> findCurrentBookingsByUser(Long userId);

    List<Booking> findItemBookingsByUser(Long userId);

    List<Booking> findItemBookingsByUserAndStatus(Long userId, BookingStatus waiting);

    List<Booking> findPastItemBookingsByUser(Long userId);

    List<Booking> findFutureItemBookingsByUser(Long userId);

    List<Booking> findCurrentItemBookingsByUser(Long userId);

    boolean findBookingDates(Long itemId, LocalDateTime start, LocalDateTime end);

    Booking getLastItemBookings(Long itemId, Long ownerId);

    Booking getNextItemBookings(Long itemId, Long ownerId);
}
