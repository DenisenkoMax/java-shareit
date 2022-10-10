package ru.practicum.shareit.requests.dto;


import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ItemRequestDtoAnswer {
    private Long id;
    private String description;
    private LocalDateTime created;
    private Set<ItemDto> items;

    public ItemRequestDtoAnswer(Long id, String description, LocalDateTime created, Set<ItemDto> items) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.items = items;
    }
}
