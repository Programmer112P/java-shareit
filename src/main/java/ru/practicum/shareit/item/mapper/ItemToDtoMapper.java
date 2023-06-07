package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemToDtoMapper {

    ItemDto modelToDto(Item item);

    List<ItemDto> modelListToDtoList(List<Item> itemList);
}
