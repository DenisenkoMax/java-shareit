package ru.practicum.shareit.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.exception.IllegalArgumentEx;

@Service
@RequiredArgsConstructor
public class Validation {
    public void validateBookingDate(BookItemRequestDto bookingDto) throws IllegalArgumentEx {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new IllegalArgumentEx("Item date wrong");
        }
    }

    public void validatePagination(int size, int from) throws IllegalArgumentEx {
        if (from < 0) {
            throw new IllegalArgumentEx("Argument from incorrect");
        } else if (size <= 0) {
            throw new IllegalArgumentEx("Argument size incorrect");
        }
    }
}
