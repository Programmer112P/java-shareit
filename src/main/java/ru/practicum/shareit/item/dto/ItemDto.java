package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.booking.dto.GetItemsBookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;
import java.util.Objects;

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

    @JsonProperty("comments")
    List<CommentDto> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(id, itemDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
