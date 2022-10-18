package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoAnswer;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests",
        consumes = MediaType.ALL_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String FIRST_ELEMENT = "0";
    private static final String PAGE_SIZE = "10";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemRequestDtoAnswer> createItemRequest(@Valid@RequestBody ItemRequestDto itemRequestDto,
                                                             @RequestHeader("X-Sharer-User-Id") long userId
                                                            )
            throws NotFoundEx {
        return new ResponseEntity<>(itemRequestService.createItemRequest(userId,itemRequestDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemRequestDtoAnswer> findItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                    @PathVariable Long id) throws NotFoundEx {
        return itemRequestService.findItemRequestById(userId, id)
                .map(request -> new ResponseEntity<>(request, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping()
    public ResponseEntity<List<ItemRequestDtoAnswer>> findUserOwnItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "from", defaultValue = FIRST_ELEMENT) int from,
            @RequestParam(name = "size", defaultValue = PAGE_SIZE) int size
    ) throws IllegalArgumentEx, NotFoundEx {
        return new ResponseEntity<>(itemRequestService.findUserOwnerItemRequests(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDtoAnswer>> findAnotherUsersItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "from", defaultValue = FIRST_ELEMENT) int from,
            @RequestParam(name = "size", defaultValue = PAGE_SIZE) int size
    ) throws IllegalArgumentEx, NotFoundEx {
        return new ResponseEntity<>(itemRequestService.findAnotherUsersItemRequests(userId, from, size), HttpStatus.OK);
    }
}
