package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/items")
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader("X-Sharer-User-Id") final Long userId,
                                                 @RequestParam(required = false, defaultValue = "0") @Min(0) final long from,
                                                 @RequestParam(required = false, defaultValue = "20") @Min(1) final int size) {
        log.info("ItemController getAll: запрос на получение всех вещей от пользователя с id {}", userId);
        ResponseEntity<Object> response = itemClient.getAllByUserId(userId, from, size);
        log.info("ItemController getAll: выполнен запрос на получение всех вещей от пользователя с id {}", userId);
        return response;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(
            @PathVariable(name = "itemId") final Long itemId,
            @RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("ItemController getById: запрос на получение вещи с id {}", itemId);
        ResponseEntity<Object> response = itemClient.getById(itemId, userId);
        log.info("ItemController getById: выполнен запрос на получение вещи с id {}", itemId);
        return response;
    }

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Valid final CreateItemDto createItemDto,
            @RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("ItemController create: запрос на создание вещи {}", createItemDto);
        ResponseEntity<Object> response = itemClient.create(createItemDto, userId);
        log.info("ItemController create: запрос на создание вещи {}", response);
        return response;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @RequestBody @Valid final ItemDto itemDto,
            @PathVariable(name = "itemId") final Long itemId,
            @RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("ItemController update: запрос на обновление вещи с id {}", itemId);
        ResponseEntity<Object> response = itemClient.update(itemDto, itemId, userId);
        log.info("ItemController update: выполнен запрос на обновление вещи с id {}", itemId);
        return response;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam(name = "text") final String text,
            @RequestParam(required = false, defaultValue = "0") @Min(0) final long from,
            @RequestParam(required = false, defaultValue = "20") @Min(1) final int size
    ) {
        log.info("ItemController search: запрос на поиск вещей по тексту \"{}\"", text);
        ResponseEntity<Object> response = itemClient.search(text, from, size);
        log.info("ItemController search: выполнен запрос на поиск вещей по тексту \"{}\"", text);
        return response;
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") final Long userId,
            @PathVariable(name = "itemId") final Long itemId,
            @RequestBody @Valid CreateCommentDto createCommentDto) {
        log.info("ItemController search: запрос на оставление комментария от пользователя {}", userId);
        ResponseEntity<Object> response = itemClient.addComment(userId, itemId, createCommentDto);
        log.info("ItemController search: выполнен запрос на оставление комментария от пользователя {}", userId);
        return response;
    }
}
