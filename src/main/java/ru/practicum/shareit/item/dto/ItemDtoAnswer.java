package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data

public class ItemDtoAnswer {
    public ItemDtoAnswer(Long id, String name, String description, Boolean available, Long ownerId,
                         BookingDto lastBooking, BookingDto nextBooking, List<CommentDto> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.comments = comments;
    }

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;

}
