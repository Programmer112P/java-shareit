package ru.practicum.shareit.booking.exception;

public class BookingAlreadyApprovedException extends RuntimeException {
    public BookingAlreadyApprovedException() {
    }

    public BookingAlreadyApprovedException(String message) {
        super(message);
    }

    public BookingAlreadyApprovedException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookingAlreadyApprovedException(Throwable cause) {
        super(cause);
    }
}
