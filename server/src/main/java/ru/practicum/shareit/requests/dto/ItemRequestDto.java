package ru.practicum.shareit.requests.dto;

import lombok.Data;

@Data
public class ItemRequestDto {
    private Long id;
    private String description;

    public ItemRequestDto(Long id, String description) {
        this.id = id;
        this.description = description;
    }
}
