package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.validation.StartAfterEnd;
import ru.practicum.shareit.request.model.Status;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@StartAfterEnd
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonProperty("itemId")
    @NotNull
    private Long itemId;

    private Long bookerId;

    private Status status;
}
