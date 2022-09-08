package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;

@Data
@Builder
public class Item {
    public Item(Long id, String name, String description, Long owner, Boolean available, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.available = available;
        this.request = request;
    }

    private Long id;
    private String name;
    private String description;
    private Long owner;
    private Boolean available;
    private ItemRequest request;

}
