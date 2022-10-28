package ru.practicum.shareit.requests;

import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoAnswer;

import java.util.List;
import java.util.Optional;

public interface ItemRequestService {
    ItemRequestDtoAnswer createItemRequest(Long userId, ItemRequestDto itemRequestDto) throws NotFoundEx;

    Optional<ItemRequestDtoAnswer> findItemRequestById(Long userId, Long id) throws NotFoundEx;

    List<ItemRequestDtoAnswer> findUserOwnerItemRequests(Long userId, int from, int size) throws NotFoundEx, IllegalArgumentEx;

    List<ItemRequestDtoAnswer> findAnotherUsersItemRequests(Long userId, int from, int size) throws NotFoundEx, IllegalArgumentEx;
}
