package ru.practicum.shareit.request;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest create(ItemRequest itemRequest, Long userId);

    List<ItemRequest> getByUserId(Long userId);

    ItemRequest getById(Long requestId, Long userId);

    List<ItemRequest> getPage(long from, int size, Long userId);
}
