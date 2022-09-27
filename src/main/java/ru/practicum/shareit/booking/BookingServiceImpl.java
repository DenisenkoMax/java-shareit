package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.user.UserRepositoryJpa;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.validation.Validation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepositoryJpa bookingRepository;
    private final UserRepositoryJpa userRepository;
    private final ItemRepositoryJpa itemRepository;
    private final Validation validation;

    public BookingDto create(BookingDto bookingDto, Long userId) throws NotFoundEx, IllegalArgumentEx {
        validation.validateUser(userId);
        validation.validateItem(bookingDto.getItemId());
        validation.validateItemAvailable(bookingDto.getItemId());
        validation.validateBookingDate(bookingDto);

        if (bookingRepository.findBookingDates(bookingDto.getItemId(), bookingDto.getStart(), bookingDto.getEnd())) {
            throw new NotFoundEx("Item dates is busy");
        }
        Booking booking = BookingMapper.toBooking(bookingDto);

        booking.setBooker(userRepository.findById(userId).get());

        booking.setItem(itemRepository.findById(bookingDto.getItemId()).get());
        validation.validateBookerIsOwner(booking.getItem(), userId);
        booking.setStatus(BookingStatus.WAITING);

        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    public BookingDtoAnswer confirmBookingRequest(Long bookingId, Long userId, boolean approved)
            throws NotFoundEx, IllegalArgumentEx {
        validation.validateBooking(bookingId);
        validation.validateUser(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        validation.validateItemOwner(booking.getItem(), userId);

        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new IllegalArgumentEx("Item alredy available");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);
        return BookingMapper.toBookingDtoAnswer(booking);
    }

    @Override
    public BookingDtoAnswer getBookingById(Long userId, Long bookingId) throws NotFoundEx {
        validation.validateUser(userId);
        validation.validateBooking(bookingId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        validation.validateUserIsBookerOrOwner(booking, userId);
        return BookingMapper.toBookingDtoAnswer(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDtoAnswer> getUserBookings(Long userId, String state) throws NotFoundEx, IllegalArgumentEx {

        validation.validateUser(userId);
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findBookingsByUser(userId);
                break;
            case "WAITING":
                bookings = bookingRepository.findBookingsByUserAndStatus(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findBookingsByUserAndStatus(userId, BookingStatus.REJECTED);
                break;
            case "PAST":
                bookings = bookingRepository.findPastBookingsByUser(userId);
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureBookingsByUser(userId);
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentBookingsByUser(userId);
                break;
            default:
                throw new IllegalArgumentEx("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(p -> BookingMapper.toBookingDtoAnswer(p))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDtoAnswer> getItemsBookings(Long userId, String state) throws NotFoundEx, IllegalArgumentEx {
        validation.validateUser(userId);
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findItemBookingsByUser(userId);
                break;
            case "WAITING":
                bookings = bookingRepository.findItemBookingsByUserAndStatus(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findItemBookingsByUserAndStatus(userId, BookingStatus.REJECTED);
                break;
            case "PAST":
                bookings = bookingRepository.findPastItemBookingsByUser(userId);
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureItemBookingsByUser(userId);
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentItemBookingsByUser(userId);
                break;
            default:
                throw new IllegalArgumentEx("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(p -> BookingMapper.toBookingDtoAnswer(p))
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    @Override
    public Booking getItemLastBookings(Long itemId, Long ownerId) {
        return bookingRepository.getLastItemBookings(itemId, ownerId);
    }

    @Transactional(readOnly = true)
    @Override
    public Booking getItemNextBookings(Long itemId, Long ownerId) {
        return bookingRepository.getNextItemBookings(itemId, ownerId);
    }
}


