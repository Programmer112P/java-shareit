package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    Booking create(Booking bookingToCreate);

    Booking approve(Boolean approved, Long bookingId, Long ownerId);

    Booking getById(Long bookingId, Long userId);

    List<Booking> getAllBookingsOfUser(Long userId, State state);

    List<Booking> getAllBookingsOfUserItems(Long userId, State state);
}
