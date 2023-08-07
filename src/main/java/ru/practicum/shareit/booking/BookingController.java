package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingController(BookingService bookingService, BookingMapper bookingMapper) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
    }

    @PostMapping
    public BookingDto create(
            @RequestBody @Valid final CreateBookingDto createBookingDto,
            @RequestHeader("X-Sharer-User-Id") final Long bookerId) {
        log.info("BookingController createBooking: запрос на бронирование {}", createBookingDto);
        //А как мне не сетать это поле? Оно приходит отдельно хэдером
        createBookingDto.setBookerId(bookerId);
        Booking bookingToCreate = bookingMapper.createDtoToModel(createBookingDto);
        Booking createdBooking = bookingService.create(bookingToCreate);
        BookingDto response = bookingMapper.modelToDto(createdBooking);
        log.info("BookingController createBooking: выполнен запрос на бронирование {}", response);
        return response;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(
            @RequestParam final Boolean approved,
            @PathVariable(name = "bookingId") final Long bookingId,
            @RequestHeader("X-Sharer-User-Id") final Long ownerId) {
        log.info("BookingController approve: запрос на подтвеждение бронирования с id {}", bookingId);
        Booking approvedBooking = bookingService.approve(approved, bookingId, ownerId);
        BookingDto response = bookingMapper.modelToDto(approvedBooking);
        log.info("BookingController approve: выполнен запрос на подтвеждение бронирования с id {}", bookingId);
        return response;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(
            @PathVariable(name = "bookingId") final Long bookingId,
            @RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("BookingController getById: запрос на получение бронирования с id {} пользователем {}", bookingId, userId);
        Booking booking = bookingService.getById(bookingId, userId);
        BookingDto response = bookingMapper.modelToDto(booking);
        log.info("BookingController getById: выполнен запрос на получение бронирования с id {} пользователем {}", bookingId, userId);
        return response;
    }

    @GetMapping
    public List<BookingDto> getAllBookingsOfUser(
            @RequestParam(required = false, defaultValue = "0") @Min(0) final long from,
            @RequestParam(required = false, defaultValue = "20") @Min(1) final int size,
            @RequestHeader("X-Sharer-User-Id") final Long userId,
            @RequestParam(required = false, defaultValue = "ALL") State state) {
        log.info("BookingController getAllBookingsOfUser: запрос на получение всех броней пользователя {}", userId);
        List<Booking> bookings = bookingService.getAllBookingsOfUser(userId, state, from, size);
        List<BookingDto> response = bookingMapper.modelListToDtoList(bookings);
        log.info("BookingController getAllBookingsOfUser: выполнен запрос на получение всех броней пользователя {}", userId);
        return response;
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsOfUserItems(
            @RequestParam(required = false, defaultValue = "0") @Min(0) final long from,
            @RequestParam(required = false, defaultValue = "20") @Min(1) final int size,
            @RequestHeader("X-Sharer-User-Id") final Long userId,
            @RequestParam(required = false, defaultValue = "ALL") State state) {
        log.info("BookingController getAllBookingsOfUser: запрос на получение всех броней на вещи пользователя {}", userId);
        List<Booking> bookings = bookingService.getAllBookingsOfUserItems(userId, state, from, size);
        List<BookingDto> response = bookingMapper.modelListToDtoList(bookings);
        log.info("BookingController getAllBookingsOfUser: выполнен запрос на получение всех броней на вещи пользователя {}", userId);
        return response;
    }
}