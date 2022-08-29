package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemDto itemDto,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {

        return itemService.create(itemDto, userId).map(newItem -> new ResponseEntity<>(newItem, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Item> updateItem(@RequestBody ItemDto itemDto,
                                           @PathVariable long itemId,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {


        return itemService.updateItem(itemDto, itemId, userId).map(itemResult -> new ResponseEntity<>(itemResult,
                        HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> findItemById(@PathVariable long itemId,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findItemById(itemId).map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Item>> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return new ResponseEntity<>(itemService.getItemsByOwner(userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Item>> findItemById(@RequestParam(defaultValue = "") String text,
                                                   @RequestHeader("X-Sharer-User-Id") long userId) {
        return new ResponseEntity<>(itemService.search(text), HttpStatus.OK);
    }
}
