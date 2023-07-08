package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.practicum.shareit.booking.validation.StartAfterEnd;
import ru.practicum.shareit.request.model.Status;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@StartAfterEnd
public class CreateBookingDto {

    public CreateBookingDto(LocalDateTime start, LocalDateTime end, Long itemId) {
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.status = Status.WAITING;
    }

    @NotNull
    @Future
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

    @JsonProperty("item_id")
    @NotNull
    private Long itemId;

    private Long bookerId;

    private Status status;
}
