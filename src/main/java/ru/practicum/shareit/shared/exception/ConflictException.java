package ru.practicum.shareit.shared.exception;

public class ConflictException extends RuntimeException {

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
