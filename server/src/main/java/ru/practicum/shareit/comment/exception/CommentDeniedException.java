package ru.practicum.shareit.comment.exception;

public class CommentDeniedException extends RuntimeException {

    public CommentDeniedException(String message) {
        super(message);
    }
}
