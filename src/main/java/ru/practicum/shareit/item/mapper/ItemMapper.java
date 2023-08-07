package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.GetItemsBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "request",
            expression = "java(createItemRequest(createItemDto.getRequestId()))")
    Item createDtoToModel(CreateItemDto createItemDto);

    @Mapping(target = "request",
            expression = "java(createItemRequest(itemDto.getRequestId()))")
    Item updateDtoToModel(ItemDto itemDto);

    @Mapping(target = "lastBooking",
            expression = "java(bookingDtoFromEntity(item.getLastBooking()))")
    @Mapping(target = "nextBooking",
            expression = "java(bookingDtoFromEntity(item.getNextBooking()))")
    @Mapping(target = "comments",
            expression = "java(commentsFromEntity(item.getComments()))")
    @Mapping(target = "requestId",
            expression = "java(getRequestId(item.getRequest()))")
    ItemDto modelToDto(Item item);

    List<ItemDto> modelListToDtoList(List<Item> itemList);

    default ItemRequest createItemRequest(Long requestId) {
        return ItemRequest.builder().id(requestId).build();
    }

    default Long getRequestId(ItemRequest itemRequest) {
        if (itemRequest == null) {
            return null;
        }
        return itemRequest.getId();
    }

    default GetItemsBookingDto bookingDtoFromEntity(Booking booking) {
        if (booking == null) {
            return null;
        }
        return new GetItemsBookingDto(booking.getId(), booking.getBooker().getId());
    }

    default List<CommentDto> commentsFromEntity(List<Comment> comments) {
        if (comments == null) {
            return Collections.emptyList();
        }
        List<CommentDto> result = new ArrayList<>();
        for (Comment c : comments) {
            result.add(CommentDto.builder()
                    .id(c.getId())
                    .authorName(c.getAuthor().getName())
                    .text(c.getText())
                    .created(c.getCreated())
                    .build());
        }
        return result;
    }
}
