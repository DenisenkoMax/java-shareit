package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * // TODO .
 */
@Data
@Builder
public class ItemDto {
    public ItemDto(String name, String description, Boolean available, Long request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }

    private String name;
    private String description;
    private Boolean available;
    private Long request;
}
