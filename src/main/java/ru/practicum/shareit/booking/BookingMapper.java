package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

public class BookingMapper {
    public static BookingDtoAnswer toBookingDtoAnswer(Booking booking) {
        if (booking != null)
            return new BookingDtoAnswer(
                    booking.getId(),
                    booking.getStart(),
                    booking.getEnd(),
                    ItemMapper.toItemDto(booking.getItem()),
                    UserMapper.toUserDtoAnswer(booking.getBooker()),
                    booking.getStatus());
        else return null;
    }

    public static BookingDto toBookingDto(Booking booking) {
        if (booking != null)
            return new BookingDto(
                    booking.getId(),
                    booking.getStart(),
                    booking.getEnd(),
                    booking.getItem().getId(),
                    booking.getBooker().getId(),
                    booking.getStatus());
        else return null;
    }

    public static Booking toBooking(BookingDto bookingDto) {
        if (bookingDto != null)
            return new Booking(
                    bookingDto.getId(),
                    bookingDto.getStart(),
                    bookingDto.getEnd(),
                    null,
                    null,
                    bookingDto.getStatus()
            );
        else return null;
    }
}
