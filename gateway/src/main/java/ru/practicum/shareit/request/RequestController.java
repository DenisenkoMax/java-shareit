package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestsClient;
    private static final String FIRST_ELEMENT = "0";
    private static final String PAGE_SIZE = "10";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                    @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.info("createItemRequest userId={}, itemRequestDto={}", userId, itemRequestDto);
        return requestsClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @PathVariable Long id) {
        log.info("findItemRequestById userId={}, id={}", userId, id);
        return requestsClient.findItemRequestById(userId, id);
    }

    @GetMapping()
    public ResponseEntity<Object> findUserOwnerItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = FIRST_ELEMENT) int from,
            @Positive @RequestParam(name = "size", defaultValue = PAGE_SIZE) int size
    ) throws IllegalArgumentEx {
        log.info("findUserOwnerItemRequests userId={}, from={}, size={}", userId, from, size);
        return requestsClient.findUserOwnerItemRequests(userId, from, size);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAnotherUsersItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = FIRST_ELEMENT) int from,
            @Positive @RequestParam(name = "size", defaultValue = PAGE_SIZE) int size
    ) throws IllegalArgumentEx {
        log.info("findAnotherUsersItemRequests userId={}, from={}, size={}", userId, from, size);
        return requestsClient.findAnotherUsersItemRequests(userId, from, size);
    }
}
