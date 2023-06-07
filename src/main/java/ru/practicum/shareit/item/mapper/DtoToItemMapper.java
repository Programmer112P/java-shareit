package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoToItemMapper {

    Item createDtoToModel(CreateItemDto createItemDto);

    List<Item> createDtoListToModelList(List<CreateItemDto> createItemDtoList);

    Item updateDtoToModel(ItemDto itemDto);

    List<Item> updateDtoListToModelList(List<ItemDto> itemDtoList);
}
