package ru.practicum.shareit.requests.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ItemRequestDto {
    private Long id;
    @NotBlank
    private String description;

    public ItemRequestDto(Long id, String description) {
        this.id = id;
        this.description = description;
    }
}
