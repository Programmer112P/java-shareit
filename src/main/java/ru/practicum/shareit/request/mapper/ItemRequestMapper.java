package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemRequestResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.PostItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface ItemRequestMapper {

    @Mapping(target = "created", expression = "java(setTime())")
    ItemRequest dtoToModel(PostItemRequestDto postItemRequestDto);

    @Mapping(target = "items",
            expression = "java(getItemDto(itemRequest.getItems()))")
    ResponseItemRequestDto modelToDto(ItemRequest itemRequest);

    List<ResponseItemRequestDto> modelListToDtoList(List<ItemRequest> itemRequestList);

    default LocalDateTime setTime() {
        return LocalDateTime.now();
    }

    default List<ItemRequestResponseDto> getItemDto(List<Item> items) {
        if (items == null) {
            return null;
        }
        List<ItemRequestResponseDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(ItemRequestResponseDto.builder()
                    .id(item.getId())
                    .requestId(item.getRequest().getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .build());
        }
        return result;
    }
}
