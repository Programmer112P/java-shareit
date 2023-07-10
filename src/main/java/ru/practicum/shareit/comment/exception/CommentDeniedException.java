package ru.practicum.shareit.comment.exception;

public class CommentDeniedException extends RuntimeException {
    public CommentDeniedException() {
    }

    public CommentDeniedException(String message) {
        super(message);
    }

    public CommentDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentDeniedException(Throwable cause) {
        super(cause);
    }
}
