package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.PostItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {

    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestMapper itemRequestMapper, ItemRequestService itemRequestService) {
        this.itemRequestMapper = itemRequestMapper;
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ResponseItemRequestDto create(
            @RequestBody final PostItemRequestDto postItemRequestDto,
            @RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("ItemRequestController create: запрос на создание itemRequest {}", postItemRequestDto);
        ItemRequest itemRequest = itemRequestMapper.dtoToModel(postItemRequestDto);
        ItemRequest createdItemRequest = itemRequestService.create(itemRequest, userId);
        ResponseItemRequestDto response = itemRequestMapper.modelToDto(createdItemRequest);
        log.info("ItemRequestController create: выполнен запрос на создание itemRequest {}", response);
        return response;
    }

    @GetMapping
    public List<ResponseItemRequestDto> getByUserId(@RequestHeader("X-Sharer-User-Id") final Long userId) {
        log.info("ItemRequestController getByUserId: запрос на получение всех запросов пользователя с id {}", userId);
        List<ItemRequest> itemRequests = itemRequestService.getByUserId(userId);
        List<ResponseItemRequestDto> response = itemRequestMapper.modelListToDtoList(itemRequests);
        log.info("ItemRequestController getByUserId: выполнен запрос на получение всех запросов пользователя с id {}", userId);
        return response;
    }

    @GetMapping("/{requestId}")
    public ResponseItemRequestDto getById(
            @RequestHeader("X-Sharer-User-Id") final Long userId,
            @PathVariable final Long requestId
    ) {
        log.info("ItemRequestController getById: запрос на получение запроса с id {}", requestId);
        ItemRequest itemRequest = itemRequestService.getById(requestId, userId);
        ResponseItemRequestDto response = itemRequestMapper.modelToDto(itemRequest);
        log.info("ItemRequestController getById: выполнен запрос на получение запроса с id {}", requestId);
        return response;
    }

    @GetMapping("/all")
    public List<ResponseItemRequestDto> getPage(
            @RequestParam(required = false, defaultValue = "0") final long from,
            //дефолтное значение не дали, поставил сам рандомное
            @RequestParam(required = false, defaultValue = "20") final int size,
            @RequestHeader("X-Sharer-User-Id") final Long userId
    ) {
        log.info("ItemRequestController getPage: запрос на получение страницы запросов from {}, size {}", from, size);
        List<ItemRequest> itemRequests = itemRequestService.getPage(from, size, userId);
        List<ResponseItemRequestDto> response = itemRequestMapper.modelListToDtoList(itemRequests);
        log.info("ItemRequestController getPage: выполнен запрос на получение страницы запросов from {}, size {}", from, size);
        return response;
    }
}
