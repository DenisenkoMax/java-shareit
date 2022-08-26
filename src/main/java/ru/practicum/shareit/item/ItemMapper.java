package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );

    }

    public static Item toItem(Long ownerId, Long itemId, ItemDto itemDto) {
        return new Item(
                itemId,
                itemDto.getName(),
                itemDto.getDescription(),
                ownerId,
                itemDto.getAvailable(),

               // item.getRequest() != null ? item.getRequest().getId() : null
        );

    }
}
