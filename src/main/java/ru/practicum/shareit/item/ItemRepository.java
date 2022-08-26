package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

interface ItemRepository {

    Item create(Item item);
    Optional<Item> findItemById(long itemId);
    List<Item> getItemsByOwner(long userId);
    Optional<Item>update(Item item, long userId);
    List<Item> search(String text);

}