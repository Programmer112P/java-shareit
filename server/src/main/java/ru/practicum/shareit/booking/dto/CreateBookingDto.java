package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CreateBookingDto {

    private LocalDateTime start;

    private LocalDateTime end;

    @JsonProperty("itemId")
    private Long itemId;

    private Long bookerId;

    private Status status = Status.WAITING;
}
