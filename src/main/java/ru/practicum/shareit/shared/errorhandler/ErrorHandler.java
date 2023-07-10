package ru.practicum.shareit.shared.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.booking.exception.BookingAlreadyApprovedException;
import ru.practicum.shareit.booking.exception.ItemUnavailableException;
import ru.practicum.shareit.comment.exception.CommentDeniedException;
import ru.practicum.shareit.shared.errorhandler.response.BadRequestResponse;
import ru.practicum.shareit.shared.errorhandler.response.ConflictResponse;
import ru.practicum.shareit.shared.errorhandler.response.NotFoundResponse;
import ru.practicum.shareit.shared.exception.AccessDeniedException;
import ru.practicum.shareit.shared.exception.ConflictException;
import ru.practicum.shareit.shared.exception.NotFoundException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundResponse handleNotFoundException(final NotFoundException e) {
        log.error("404 {}", e.getMessage());
        return new NotFoundResponse(e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ConflictResponse handleConflictResponse(final ConflictException e) {
        log.error("409 {}", e.getMessage());
        return new ConflictResponse(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BadRequestResponse handleAccessDeniedException(final AccessDeniedException e) {
        log.error("404 {}", e.getMessage());
        return new BadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(BookingAlreadyApprovedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestResponse handleBookingAlreadyApprovedException(final BookingAlreadyApprovedException e) {
        log.error("400 {}", e.getMessage());
        return new BadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(CommentDeniedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestResponse handleCommentDeniedException(final CommentDeniedException e) {
        log.error("400 {}", e.getMessage());
        return new BadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestResponse handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.error("400 {}", e.getMessage());
        String state = e.getMessage().substring(e.getMessage().lastIndexOf(".") + 1);
        return new BadRequestResponse("Unknown state: " + state);
    }

    @ExceptionHandler(ItemUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestResponse handleItemUnavailableException(final ItemUnavailableException e) {
        log.error("400 {}", e.getMessage());
        return new BadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("400 {}", e.getMessage());
        return new BadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestResponse handleHttpMessageConversionException(final HttpMessageConversionException e) {
        log.error("400 {}", e.getMessage());
        return new BadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestResponse handleMissingRequestHeaderException(final MissingRequestHeaderException e) {
        log.error("400 {}", e.getMessage());
        return new BadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestResponse handleConstraintViolationException(final ConstraintViolationException e) {
        log.error("400 {}", e.getMessage());
        return new BadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestResponse handleMissingParams(final MissingServletRequestParameterException e) {
        log.error("400 {}", e.getMessage());
        return new BadRequestResponse(e.getMessage());
    }
}
