package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface BookingRepositoryJpaCustom {
    Page<Booking> findBookingsByUser(Long userId, Pageable pageable);

    Page<Booking> findBookingsByUserAndStatus(Long userId, BookingStatus status, Pageable pageable);

    Page<Booking> findPastBookingsByUser(Long userId, Pageable pageable);

    Page<Booking> findFutureBookingsByUser(Long userId, Pageable pageable);

    Page<Booking> findCurrentBookingsByUser(Long userId, Pageable pageable);

    Page<Booking> findItemBookingsByUser(Long userId, Pageable pageable);

    Page<Booking> findItemBookingsByUserAndStatus(Long userId, BookingStatus waiting, Pageable pageable);

    Page<Booking> findPastItemBookingsByUser(Long userId, Pageable pageable);

    Page<Booking> findFutureItemBookingsByUser(Long userId, Pageable pageable);

    Page<Booking> findCurrentItemBookingsByUser(Long userId, Pageable pageable);

    boolean findBookingDates(Long itemId, LocalDateTime start, LocalDateTime end);

    Booking getLastItemBookings(Long itemId, Long ownerId);

    Booking getNextItemBookings(Long itemId, Long ownerId);
}
