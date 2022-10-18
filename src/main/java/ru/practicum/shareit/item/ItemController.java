package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.IllegalArgumentEx;
import ru.practicum.shareit.exception.NotFoundEx;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoAnswer;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(
        value = "/items",
        consumes = MediaType.ALL_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ItemController {
    private final ItemService itemService;
    private static final String FIRST_ELEMENT = "0";
    private static final String PAGE_SIZE = "10";


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto,
                                              @RequestHeader("X-Sharer-User-Id") long userId) throws NotFoundEx {

        return itemService.create(itemDto, userId).map(newItem -> new ResponseEntity<>(newItem, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestBody ItemDto itemDto,
                                              @PathVariable long itemId,
                                              @RequestHeader("X-Sharer-User-Id") long userId) throws NotFoundEx {


        return itemService.updateItem(itemDto, itemId, userId).map(itemResult -> new ResponseEntity<>(itemResult,
                        HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDtoAnswer> findItemById(@PathVariable long itemId,
                                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findItemById(userId, itemId).map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<ItemDtoAnswer>> getItems(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "from", defaultValue = FIRST_ELEMENT) int from,
            @RequestParam(name = "size", defaultValue = PAGE_SIZE) int size
    )
            throws NotFoundEx, IllegalArgumentEx {
        return new ResponseEntity<>(itemService.getItemsByOwner(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> findItemById(
            @RequestParam(defaultValue = "") String text,
            @RequestParam(name = "from", defaultValue = FIRST_ELEMENT) int from,
            @RequestParam(name = "size", defaultValue = PAGE_SIZE) int size,
            @RequestHeader("X-Sharer-User-Id") long userId)
            throws IllegalArgumentEx {
        return new ResponseEntity<>(itemService.search(text, from, size), HttpStatus.OK);
    }

    @PostMapping(path = "/{itemId}/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDto> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PathVariable Long itemId,
                                                    @Valid @RequestBody CommentDto commentDto)
            throws IllegalArgumentEx, NotFoundEx {
        return new ResponseEntity<>(itemService.createComment(userId, itemId, commentDto), HttpStatus.OK);
    }
}
