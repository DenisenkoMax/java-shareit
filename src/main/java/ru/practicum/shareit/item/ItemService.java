package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoAnswer;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Optional<ItemDto> create(ItemDto itemDto, long userId) throws NotFoundEx;

    Optional<ItemDto> updateItem(ItemDto itemDto, long itemId, long userId) throws NotFoundEx;

    Optional<ItemDtoAnswer> findItemById(long userId, long itemId);

    List<ItemDtoAnswer> getItemsByOwner(long userId, int from, int size) throws NotFoundEx, IllegalArgumentEx;

    List<ItemDto> search(String text, int from, int size) throws IllegalArgumentEx;

    CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) throws NotFoundEx, IllegalArgumentEx;
}
