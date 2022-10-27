package ru.practicum.shareit.requests;

import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoAnswer;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.stream.Collectors;


public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        if (itemRequestDto == null) return null;
        else return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                null,
                null,
                null
        );
    }

    public static ItemRequestDtoAnswer toItemRequestDtoAnswer(ItemRequest itemRequest) {
        if (itemRequest == null) return null;
        else return new ItemRequestDtoAnswer(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                (itemRequest.getItems() == null) ? null
                        : itemRequest.getItems().stream()
                        .map(p -> ItemMapper.toItemDto(p))
                        .collect(Collectors.toSet())
        );
    }
}
