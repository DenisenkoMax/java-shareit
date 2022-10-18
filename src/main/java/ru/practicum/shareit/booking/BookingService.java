package ru.practicum.shareit.booking;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;

import java.util.List;

public interface BookingService {
    BookingDtoAnswer create(BookingDto bookingDto, Long userId) throws NotFoundEx, IllegalArgumentEx;

    BookingDtoAnswer confirmBookingRequest(Long bookingId, Long userId, boolean approved)
            throws NotFoundEx, IllegalArgumentEx;

    BookingDtoAnswer getBookingById(Long userId, Long bookingId) throws NotFoundEx;

    List<BookingDtoAnswer> getUserBookings(Long userId, String state, int from, int size) throws NotFoundEx,
            IllegalArgumentEx;

    List<BookingDtoAnswer> getItemsBookings(Long userId, String state, int from, int size) throws NotFoundEx,
            IllegalArgumentEx;

    @Transactional(readOnly = true)
    Booking getItemLastBookings(Long itemId, Long ownerId);

    @Transactional(readOnly = true)
    Booking getItemNextBookings(Long itemId, Long ownerId);
}
