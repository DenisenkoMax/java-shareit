package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoAnswer;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Optional<Item> create(ItemDto itemDto, long userId) throws  NotFoundEx;

    Optional<ItemDto> updateItem(ItemDto itemDto, long itemId, long userId) throws NotFoundEx;

    Optional<ItemDtoAnswer> findItemById(long userId, long itemId);

    List<ItemDtoAnswer> getItemsByOwner(long userId) throws NotFoundEx;

    List<ItemDto> search(String text);

    CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) throws NotFoundEx, IllegalArgumentEx;
}
