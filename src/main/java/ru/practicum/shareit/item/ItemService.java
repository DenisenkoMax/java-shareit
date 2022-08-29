package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Optional<Item> create(ItemDto itemDto, long userId);

    Optional<Item> updateItem(ItemDto itemDto, long itemId, long userId);

    Optional<Item> findItemById(long itemId);

    List<Item> getItemsByOwner(long userId);

    List<Item> search(String text);

}
