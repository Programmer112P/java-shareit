package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<Item> getAllByUserId(Long userId, Long from, Integer size);

    Item getById(Long itemId, Long userId);

    Item create(Item itemToCreate, Long userId);

    Item update(Item newItem, Long itemId, Long userId);

    List<Item> search(String text, Long from, Integer size);

    Comment addComment(Long userId, Long itemId, Comment comment);
}
