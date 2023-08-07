package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.booking.dto.GetItemsBookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ItemDto {

    public ItemDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private GetItemsBookingDto lastBooking;

    private GetItemsBookingDto nextBooking;

    private Long requestId;

    @JsonProperty("comments")
    List<CommentDto> comments;
}
