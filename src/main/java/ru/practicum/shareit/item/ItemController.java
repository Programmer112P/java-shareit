package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    /*
     * Вопрос по контроллерам - что лучше возвращать - ResponseEntity или просто Dto?
     * */
    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("ItemController getAll: запрос на получение всех вещей от пользователя с id {}", userId);
        List<Item> itemList = itemService.getAllByUserId(userId);
        List<ItemDto> response = itemMapper.modelListToDtoList(itemList);
        log.info("ItemController getAll: выполнен запрос на получение всех вещей от пользователя с id {}", userId);
        return response;
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(
            @PathVariable(name = "itemId") final Long itemId,
            @RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("ItemController getById: запрос на получение вещи с id {}", itemId);
        Item item = itemService.getById(itemId, userId);
        ItemDto response = itemMapper.modelToDto(item);
        log.info("ItemController getById: выполнен запрос на получение вещи с id {}", itemId);
        return response;
    }

    @PostMapping
    public ItemDto create(
            @RequestBody @Valid final CreateItemDto createItemDto,
            @RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("ItemController create: запрос на создание вещи {}", createItemDto);
        Item itemToCreate = itemMapper.createDtoToModel(createItemDto);
        Item item = itemService.create(itemToCreate, userId);
        ItemDto response = itemMapper.modelToDto(item);
        log.info("ItemController create: запрос на создание вещи {}", response);
        return response;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestBody @Valid final ItemDto itemDto,
            @PathVariable(name = "itemId") final Long itemId,
            @RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("ItemController update: запрос на обновление вещи с id {}", itemId);
        Item newItem = itemMapper.updateDtoToModel(itemDto);
        Item updatedItem = itemService.update(newItem, itemId, userId);
        ItemDto response = itemMapper.modelToDto(updatedItem);
        log.info("ItemController update: выполнен запрос на обновление вещи с id {}", itemId);
        return response;
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text") final String text) {
        log.info("ItemController search: запрос на поиск вещей по тексту \"{}\"", text);
        List<Item> items = itemService.search(text);
        List<ItemDto> response = itemMapper.modelListToDtoList(items);
        log.info("ItemController search: выполнен запрос на поиск вещей по тексту \"{}\"", text);
        return response;
    }
}
