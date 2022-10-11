package ru.practicum.shareit.item.dto;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;
    @NotNull
    @NotEmpty
    private String text;
    private Long itemId;
    private String authorName;
    private LocalDateTime created;

    public CommentDto(Long id, String text, Long itemId, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.itemId = itemId;
        this.authorName = authorName;
        this.created = created;
    }
}
