package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAnswer;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto,
                                                    @RequestHeader("X-Sharer-User-Id") long userId)
            throws IllegalArgumentEx, NotFoundEx {
        return new ResponseEntity<>(bookingService.create(bookingDto, userId), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{bookingId}")
    public ResponseEntity<BookingDtoAnswer> handleBookingRequest(@PathVariable Long bookingId,
                                                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                                                 @RequestParam boolean approved)
            throws IllegalArgumentEx, NotFoundEx {
        return new ResponseEntity<>(bookingService.confirmBookingRequest(bookingId, userId, approved), HttpStatus.OK);
    }

    @GetMapping(path = "/{bookingId}")
    public ResponseEntity<BookingDtoAnswer> getBookingById(@PathVariable Long bookingId,
                                                           @RequestHeader("X-Sharer-User-Id") long userId)
            throws NotFoundEx {
        return new ResponseEntity<>(bookingService.getBookingById(userId, bookingId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<BookingDtoAnswer>> getUserBookings(@RequestParam(defaultValue = "ALL") String state,
                                                                  @RequestHeader("X-Sharer-User-Id") Long userId)
            throws NotFoundEx, IllegalArgumentEx {
        return new ResponseEntity<>(bookingService.getUserBookings(userId, state), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDtoAnswer>> getAllItemsBookings(@RequestParam(defaultValue = "ALL") String state,
                                                                      @RequestHeader("X-Sharer-User-Id") Long userId)
            throws IllegalArgumentEx, NotFoundEx {
        return new ResponseEntity<>(bookingService.getItemsBookings(userId, state), HttpStatus.OK);
    }
}
