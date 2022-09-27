package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDtoAnswer;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@Builder

public class BookingDtoAnswer {
    private Long id;
    @FutureOrPresent
    private LocalDateTime start;

    private LocalDateTime end;
    private ItemDto item;

    private UserDtoAnswer booker;
    private BookingStatus status;

    public BookingDtoAnswer(Long id, LocalDateTime start, LocalDateTime end, ItemDto item, UserDtoAnswer booker,
                            BookingStatus status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }
}
