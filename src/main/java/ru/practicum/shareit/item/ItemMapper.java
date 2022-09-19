package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoAnswer;
import ru.practicum.shareit.item.model.Item;

import java.util.Set;

public class ItemMapper {
    public static ItemDtoAnswer toItemDtoAnswer(Item item, BookingDto last, BookingDto next, Set<CommentDto> commentDto) {
        if (item != null)
            return new ItemDtoAnswer(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getOwner().getId(),
                    last,
                    next,
                    commentDto
            );
        else return null;
    }

    public static ItemDto toItemDto(Item item) {
        if (item != null)
            return new ItemDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable()
            );
        else return null;
    }

    public static Item toItem(ItemDto itemDto) {
        if (itemDto != null)
            return new Item(
                    itemDto.getId(),
                    itemDto.getName(),
                    itemDto.getDescription(),
                    null,
                    itemDto.getAvailable(),
                    null);
        else return null;
    }
}