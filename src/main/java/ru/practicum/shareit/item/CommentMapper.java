package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    public static Comment toComment(CommentDto commentDto) {
        if (commentDto == null) return null;
        else return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                null,
                null,
                null
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        if (comment == null) return null;
        else return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}
