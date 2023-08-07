package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CreateCommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public ItemController(ItemService itemService, ItemMapper itemMapper, CommentMapper commentMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.commentMapper = commentMapper;
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") final Long userId,
                                        @RequestParam(required = false, defaultValue = "0") @Min(0) final long from,
                                        @RequestParam(required = false, defaultValue = "20") @Min(1) final int size) {
        log.info("ItemController getAll: запрос на получение всех вещей от пользователя с id {}", userId);
        List<Item> itemList = itemService.getAllByUserId(userId, from, size);
        List<ItemDto> response = itemMapper.modelListToDtoList(itemList);
        response.sort(Comparator.comparing(ItemDto::getId));//В тестах иначе не проходит, по идее это не нужно
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
    public List<ItemDto> search(
            @RequestParam(name = "text") final String text,
            @RequestParam(required = false, defaultValue = "0") @Min(0) final long from,
            @RequestParam(required = false, defaultValue = "20") @Min(1) final int size
    ) {
        log.info("ItemController search: запрос на поиск вещей по тексту \"{}\"", text);
        List<Item> items = itemService.search(text, from, size);
        List<ItemDto> response = itemMapper.modelListToDtoList(items);
        log.info("ItemController search: выполнен запрос на поиск вещей по тексту \"{}\"", text);
        return response;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @RequestHeader("X-Sharer-User-Id") final Long userId,
            @PathVariable(name = "itemId") final Long itemId,
            @RequestBody @Valid CreateCommentDto createCommentDto) {
        log.info("ItemController search: запрос на оставление комментария от пользователя {}", userId);
        Comment commentToCreate = commentMapper.createDtoToModel(createCommentDto);
        Comment createdComment = itemService.addComment(userId, itemId, commentToCreate);
        CommentDto response = commentMapper.modelToDto(createdComment);
        log.info("ItemController search: выполнен запрос на оставление комментария от пользователя {}", userId);
        return response;
    }
}
