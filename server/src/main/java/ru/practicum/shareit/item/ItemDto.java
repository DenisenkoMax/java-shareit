package ru.practicum.shareit.item;

import lombok.Data;

@Data
public class ItemDto {
    public ItemDto(Long id, String name, String description, Boolean available, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
