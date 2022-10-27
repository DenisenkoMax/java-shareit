package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private static final String FIRST_ELEMENT = "0";
    private static final String PAGE_SIZE = "10";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("createItem itemId={}, userId={}", itemDto, userId);
        return itemClient.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Valid @RequestBody UpdateItemDto itemDto,
                                             @PathVariable long itemId,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("updateItem itemDto={}, itemId={}, userId={}", itemDto, itemId, userId);
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@PathVariable long itemId,
                                               @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("findItemById itemId={}, userId={}", itemId, userId);
        return itemClient.findItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = FIRST_ELEMENT) int from,
            @Positive @RequestParam(name = "size", defaultValue = PAGE_SIZE) int size
    ) {
        log.info("getItemsByOwner userId={}, from={}, size={}", userId, from, size);
        return itemClient.getItemsByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam(defaultValue = "") String text,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = FIRST_ELEMENT) int from,
            @Positive @RequestParam(name = "size", defaultValue = PAGE_SIZE) int size,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("search item text={}, from={}, size={}", text, userId, from, size);
        return itemClient.search(text, userId, from, size);
    }

    @PostMapping(path = "/{itemId}/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long itemId,
                                                @Valid @RequestBody CommentDto commentDto) {
        log.info("createComment userId={}, itemId={}, commentDto={}", userId, itemId, commentDto);
        return itemClient.createComment(userId, itemId, commentDto);
    }
}
