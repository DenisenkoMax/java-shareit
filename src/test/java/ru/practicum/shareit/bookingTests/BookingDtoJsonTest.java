package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
public class BookingDtoJsonTest {
    @Autowired
    private JacksonTester<BookingDto> jacksonTester;

    @Test
    void serializedTest() throws IOException {
        LocalDateTime start = LocalDateTime.of(2022, Month.OCTOBER, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, Month.OCTOBER, 3, 0, 0);
        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L, BookingStatus.APPROVED);
        JsonContent<BookingDto> result = jacksonTester.write(bookingDto);
        then(result).extractingJsonPathValue("$.id").isEqualTo(1);
        then(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-10-01T00:00:00");
        then(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-10-03T00:00:00");
        then(result).extractingJsonPathValue("$.itemId").isEqualTo(1);
        then(result).extractingJsonPathValue("$.bookerId").isEqualTo(1);
        then(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
    }
}
