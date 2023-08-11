package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;


    @GetMapping
    public ResponseEntity<Object> getAllBookingsOfUser(
            @RequestParam(required = false, defaultValue = "0") @Min(0) final int from,
            @RequestParam(required = false, defaultValue = "20") @Min(1) final int size,
            @RequestHeader("X-Sharer-User-Id") final Long userId,
            @RequestParam(required = false, defaultValue = "ALL") State state) {
        log.info("BookingController getAllBookingsOfUser: запрос на получение всех броней пользователя {}", userId);
        ResponseEntity<Object> response = bookingClient.getAllBookingsOfUser(userId, state, from, size);
        log.info("BookingController getAllBookingsOfUser: выполнен запрос на получение всех броней пользователя {}", userId);
        return response;
    }

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Valid final CreateBookingDto createBookingDto,
            @RequestHeader("X-Sharer-User-Id") final Long bookerId) {
        log.info("BookingController createBooking: запрос на бронирование {}", createBookingDto);
        ResponseEntity<Object> response = bookingClient.bookItem(createBookingDto, bookerId);
        log.info("BookingController createBooking: выполнен запрос на бронирование {}", response);
        return response;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(
            @PathVariable(name = "bookingId") final Long bookingId,
            @RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("BookingController getById: запрос на получение бронирования с id {} пользователем {}", bookingId, userId);
        ResponseEntity<Object> response = bookingClient.getBooking(userId, bookingId);
        log.info("BookingController getById: выполнен запрос на получение бронирования с id {} пользователем {}", bookingId, userId);
        return response;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsOfUserItems(
            @RequestParam(required = false, defaultValue = "0") @Min(0) final long from,
            @RequestParam(required = false, defaultValue = "20") @Min(1) final int size,
            @RequestHeader("X-Sharer-User-Id") final Long userId,
            @RequestParam(required = false, defaultValue = "ALL") State state) {
        log.info("BookingController getAllBookingsOfUser: запрос на получение всех броней на вещи пользователя {}", userId);
        ResponseEntity<Object> response = bookingClient.getAllBookingsOfUserItems(userId, state, from, size);
        log.info("BookingController getAllBookingsOfUser: выполнен запрос на получение всех броней на вещи пользователя {}", userId);
        return response;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(
            @RequestParam final Boolean approved,
            @PathVariable(name = "bookingId") final Long bookingId,
            @RequestHeader("X-Sharer-User-Id") final Long ownerId) {
        log.info("BookingController approve: запрос на подтвеждение бронирования с id {}", bookingId);
        ResponseEntity<Object> response = bookingClient.approve(approved, bookingId, ownerId);
        log.info("BookingController approve: выполнен запрос на подтвеждение бронирования с id {}", bookingId);
        return response;
    }
}
