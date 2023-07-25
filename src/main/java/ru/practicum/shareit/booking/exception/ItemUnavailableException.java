package ru.practicum.shareit.booking.exception;

public class ItemUnavailableException extends RuntimeException {
    public ItemUnavailableException() {
    }

    public ItemUnavailableException(String message) {
        super(message);
    }

    public ItemUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemUnavailableException(Throwable cause) {
        super(cause);
    }
}
