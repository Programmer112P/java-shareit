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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item createDtoToModel(CreateItemDto createItemDto);

    List<Item> createDtoListToModelList(List<CreateItemDto> createItemDtoList);

    Item updateDtoToModel(ItemDto itemDto);

    List<Item> updateDtoListToModelList(List<ItemDto> itemDtoList);

    @Mapping(target = "lastBooking",
            expression = "java(bookingDtoFromEntity(item.getLastBooking()))")
    @Mapping(target = "nextBooking",
            expression = "java(bookingDtoFromEntity(item.getNextBooking()))")
    @Mapping(target = "comments",
            expression = "java(commentsFromEntity(item.getComments()))")
    ItemDto modelToDto(Item item);

    List<ItemDto> modelListToDtoList(List<Item> itemList);

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
