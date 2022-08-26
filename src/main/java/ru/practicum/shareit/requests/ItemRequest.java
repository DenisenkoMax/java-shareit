package ru.practicum.shareit.requests;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
@Builder
public class ItemRequest {
    private Long id;
    private String description;
    private Long requestor;
    private LocalDateTime created;
}