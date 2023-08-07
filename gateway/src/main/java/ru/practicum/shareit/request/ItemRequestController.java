package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.PostItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @Autowired
    public ItemRequestController(ItemRequestClient itemRequestClient) {
        this.itemRequestClient = itemRequestClient;
    }

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Valid final PostItemRequestDto postItemRequestDto,
            @RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("ItemRequestController create: запрос на создание itemRequest {}", postItemRequestDto);
        ResponseEntity<Object> response = itemRequestClient.create(postItemRequestDto, userId);
        log.info("ItemRequestController create: выполнен запрос на создание itemRequest {}", response);
        return response;
    }

    @GetMapping
    public ResponseEntity<Object> getByUserId(@RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("ItemRequestController getByUserId: запрос на получение всех запросов пользователя с id {}", userId);
        ResponseEntity<Object> response = itemRequestClient.getByUserId(userId);
        log.info("ItemRequestController getByUserId: выполнен запрос на получение всех запросов пользователя с id {}", userId);
        return response;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(
            @RequestHeader("X-Sharer-User-Id") final Long userId,
            @PathVariable final Long requestId
    ) {
        log.info("ItemRequestController getById: запрос на получение запроса с id {}", requestId);
        ResponseEntity<Object> response = itemRequestClient.getById(userId, requestId);
        log.info("ItemRequestController getById: выполнен запрос на получение запроса с id {}", requestId);
        return response;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getPage(
            @RequestParam(required = false, defaultValue = "0") @Min(0) final long from,
            @RequestParam(required = false, defaultValue = "20") @Min(1) final int size,
            @RequestHeader("X-Sharer-User-Id") final Long userId
    ) {
        log.info("ItemRequestController getPage: запрос на получение страницы запросов from {}, size {}", from, size);
        ResponseEntity<Object> response = itemRequestClient.getPage(from, size, userId);
        log.info("ItemRequestController getPage: выполнен запрос на получение страницы запросов from {}, size {}", from, size);
        return response;
    }
}
