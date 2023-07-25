package ru.practicum.shareit.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CreateCommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment createDtoToModel(CreateCommentDto createCommentDto);

    @Mapping(target = "authorName",
            expression = "java(nameFromAuthor(comment.getAuthor()))")
    CommentDto modelToDto(Comment comment);

    default String nameFromAuthor(User author) {
        return author.getName();
    }
}
