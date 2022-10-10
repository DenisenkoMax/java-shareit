package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoAnswer;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserRepositoryJpa;
import ru.practicum.shareit.validation.Validation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepositoryJpa itemRequestRepository;
    private final UserRepositoryJpa userRepository;
    private final Validation validation;


    @Override
    public ItemRequestDtoAnswer createItemRequest(Long userId, ItemRequestDto itemRequestDto) throws NotFoundEx {
            validation.validateUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(userRepository.findById(userId).get());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDtoAnswer(itemRequest);
    }

    @Override
    public Optional<ItemRequestDtoAnswer> findItemRequestById(Long userId, Long id) throws NotFoundEx {
        validation.validateUser(userId);
        return (itemRequestRepository.findById(id).isPresent())
                ?Optional.ofNullable(ItemRequestMapper.toItemRequestDtoAnswer(itemRequestRepository.findById(id)
                .get()))
                :Optional.empty(); }

    @Override
    public List<ItemRequestDtoAnswer> findUserOwnerItemRequests(Long userId, int from, int size) throws NotFoundEx,
            IllegalArgumentEx {
        validation.validateUser(userId);
        validation.validatePagination(size, from);
        return itemRequestRepository
                .findItemRequestsByUser(userId, PageRequest.of(from / size, size, Sort.by("created")
                        .descending())).stream().map(p -> ItemRequestMapper.toItemRequestDtoAnswer(p))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDtoAnswer> findAnotherUsersItemRequests(Long userId, int from, int size) throws NotFoundEx,
            IllegalArgumentEx {
        validation.validateUser(userId);
        validation.validatePagination(size, from);
        return itemRequestRepository
                .findItemRequestsByAnotherUsers(userId, PageRequest.of(from / size, size, Sort.by("created")
                        .descending())).stream().map(p -> ItemRequestMapper.toItemRequestDtoAnswer(p))
                .collect(Collectors.toList());
    }
}
