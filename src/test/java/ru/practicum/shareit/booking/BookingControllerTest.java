package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.request.model.Status;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/test-schema.sql", "/data.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void create_shouldReturnCorrectJson_whenCorrectId() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(start)
                .end(end)
                .itemId(1L)
                .status(Status.WAITING)
                .build();

        String requestBody = objectMapper.writeValueAsString(createBookingDto);

        mockMvc.perform(post("/bookings")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 2L)
                        .content(requestBody))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(4),
                        jsonPath("$.start").isNotEmpty(),
                        jsonPath("$.end").isNotEmpty()
                );
    }

    @Test
    void create_shouldReturn404_whenNotFoundUserId() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(start)
                .end(end)
                .itemId(1L)
                .status(Status.WAITING)
                .build();

        String requestBody = objectMapper.writeValueAsString(createBookingDto);

        mockMvc.perform(post("/bookings")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 10000L)
                        .content(requestBody))
                .andExpectAll(status().isNotFound());
    }

    @Test
    void create_shouldReturn404_whenNotFoundItemId() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(start)
                .end(end)
                .itemId(13333L)
                .status(Status.WAITING)
                .build();

        String requestBody = objectMapper.writeValueAsString(createBookingDto);

        mockMvc.perform(post("/bookings")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 3)
                        .content(requestBody))
                .andExpectAll(status().isNotFound());
    }

    @Test
    void create_shouldReturn400_whenNotAvailableItem() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(5);
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(start)
                .end(end)
                .itemId(2L)
                .status(Status.WAITING)
                .build();

        String requestBody = objectMapper.writeValueAsString(createBookingDto);

        mockMvc.perform(post("/bookings")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 3)
                        .content(requestBody))
                .andExpectAll(status().isBadRequest());
    }

    @Test
    void approve_shouldReturnApprovedStatus_whenApprove() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .param("approved", "true")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value(Status.APPROVED.toString())
                );
    }

    @Test
    void approve_shouldReturnRejectStatus_whenReject() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .param("approved", "false")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value(Status.REJECTED.toString())
                );
    }

    @Test
    void approve_shouldReturn400_whenAlreadyApprovedBooking() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", 3)
                        .param("approved", "false")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 3))
                .andExpectAll(status().isBadRequest());
    }

    @Test
    void approve_shouldReturn404_whenNotFoundUserId() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", 3)
                        .param("approved", "false")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 123))
                .andExpectAll(status().isNotFound());
    }

    @Test
    void approve_shouldReturn404_whenNotFoundBookingId() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", 1233)
                        .param("approved", "false")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(status().isNotFound());
    }

    @Test
    void getById_shouldReturn200_whenCorrectRequest() throws Exception {
        mockMvc.perform(get("/bookings/{bookingId}", 3)
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 3))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value(Status.APPROVED.toString()),
                        jsonPath("$.start").value("2022-01-01T11:30:30"),
                        jsonPath("$.end").value("2022-01-10T11:30:30")
                );
    }

    @Test
    void getById_shouldReturn404_whenNotFoundBookingId() throws Exception {
        mockMvc.perform(get("/bookings/{bookingId}", 1234)
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 3))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void getById_shouldReturn404_whenNotAccessDenied() throws Exception {
        mockMvc.perform(get("/bookings/{bookingId}", 3)
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 2))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void getAllBookingsOfUser_shouldReturn200_whenCorrectRequest() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 3))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[0].status").value(Status.WAITING.toString()),
                        jsonPath("$.[0].start").value("2021-10-10T11:30:30"),
                        jsonPath("$.[0].end").value("2021-10-12T11:30:30")
                );
    }

    @Test
    void getAllBookingsOfUser_shouldReturnPast_whenPastRequest() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("Content-Type", "application/json")
                        .param("state", "PAST")
                        .header("X-Sharer-User-Id", 3))
                .andExpectAll(
                        status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void getAllBookingsOfUser_shouldReturnFuture_whenFutureRequest() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("Content-Type", "application/json")
                        .param("state", "FUTURE")
                        .header("X-Sharer-User-Id", 3))
                .andExpectAll(
                        status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void getAllBookingsOfUser_shouldReturnRejected_whenRejectedRequest() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("Content-Type", "application/json")
                        .param("state", "REJECTED")
                        .header("X-Sharer-User-Id", 2))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[0].status").value(Status.REJECTED.toString()),
                        jsonPath("$.[0].start").value("2022-10-10T11:30:30"),
                        jsonPath("$.[0].end").value("2022-10-12T11:30:30")
                );
    }

    @Test
    void getAllBookingsOfUser_shouldReturnWaiting_whenWaitingRequest() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("Content-Type", "application/json")
                        .param("state", "WAITING")
                        .header("X-Sharer-User-Id", 2))
                .andExpectAll(
                        status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void getAllBookingsOfUser_shouldReturnCurrent_whenCurrentRequest() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("Content-Type", "application/json")
                        .param("state", "CURRENT")
                        .header("X-Sharer-User-Id", 2))
                .andExpectAll(
                        status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void getAllBookingsOfUser_shouldReturn404_whenNotFoundUserId() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("Content-Type", "application/json")
                        .param("state", "CURRENT")
                        .header("X-Sharer-User-Id", 222222))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void getAllBookingsOfUser_shouldReturn400_whenInvalidPageSize() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("Content-Type", "application/json")
                        .param("state", "CURRENT")
                        .param("size", "-1")
                        .header("X-Sharer-User-Id", 222222))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void getAllBookingsOfUser_shouldReturn400_whenUnknownState() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("Content-Type", "application/json")
                        .param("state", "UNKNOWN")
                        .header("X-Sharer-User-Id", 2))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void getAllBookingsOfUserItems_shouldReturnAll_whenAllRequest() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[0].booker.id").value(2),
                        jsonPath("$.[1].booker.id").value(3)
                );
    }

    @Test
    void getAllBookingsOfUserItems_shouldReturnPast_whenPastRequest() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("Content-Type", "application/json")
                        .param("state", "PAST")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[0].booker.id").value(2),
                        jsonPath("$.[1].booker.id").value(3)
                );
    }

    @Test
    void getAllBookingsOfUserItems_shouldReturnFuture_whenFutureRequest() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("Content-Type", "application/json")
                        .param("state", "FUTURE")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(
                        status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void getAllBookingsOfUserItems_shouldReturnCurrent_whenCurrentRequest() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("Content-Type", "application/json")
                        .param("state", "CURRENT")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(
                        status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void getAllBookingsOfUserItems_shouldReturnWaiting_whenWaitingRequest() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("Content-Type", "application/json")
                        .param("state", "WAITING")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[0].booker.id").value(3),
                        jsonPath("$.[0].status").value(Status.WAITING.toString())
                );
    }

    @Test
    void getAllBookingsOfUserItems_shouldReturnRejected_whenRejectedRequest() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("Content-Type", "application/json")
                        .param("state", "REJECTED")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.[0].booker.id").value(2),
                        jsonPath("$.[0].status").value(Status.REJECTED.toString())
                );
    }

    @Test
    void getAllBookingsOfUserItems_shouldReturn404_whenNotFoundId() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("Content-Type", "application/json")
                        .param("state", "REJECTED")
                        .header("X-Sharer-User-Id", 12345))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void getAllBookingsOfUserItems_shouldReturn400_whenInvalidFromParam() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("Content-Type", "application/json")
                        .param("state", "REJECTED")
                        .param("from", "-1")
                        .header("X-Sharer-User-Id", 1))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void getAllBookingsOfUserItems_shouldReturnEmptyList_whenUserDontOwnAnyItem() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("Content-Type", "application/json")
                        .header("X-Sharer-User-Id", 2))
                .andExpectAll(
                        status().isOk(),
                        content().json("[]")
                );
    }
}