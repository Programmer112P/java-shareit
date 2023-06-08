package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.shared.exception.NotFoundException;
import ru.practicum.shareit.user.EmbeddedUserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final EmbeddedItemRepository itemRepository;
    private final EmbeddedUserRepository userRepository;

    @Autowired
    public ItemServiceImpl(EmbeddedItemRepository repository, EmbeddedUserRepository userRepository) {
        this.itemRepository = repository;
        this.userRepository = userRepository;
    }

    /*
     * Мне не нравится возвращать просто пустой список, если пользователя не существует
     * У нас где-то в логике программы есть ошибка, а возвращается 200
     *  */
    @Override
    public List<Item> getAllByUserId(final Long userId) {
        log.info("ItemService getAllByUserId: запрос на получение всех вещей пользователя с id {}", userId);
        Optional<User> optionalUser = userRepository.getById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("При получении списка вещей передан несуществующий id пользователя");
        }
        List<Item> items = itemRepository.getAllByUserId(userId);
        log.info("ItemService getAllByUserId: запрос на получение всех вещей пользователя с id {}", userId);
        return items;
    }

    @Override
    public Item getById(final Long itemId, final Long userId) {
        log.info("ItemService getById: запрос на получение вещи по id {}", itemId);
        Optional<Item> optionalItem = itemRepository.getById(itemId);
        if (optionalItem.isEmpty()) {
            throw new NotFoundException("При запросе на получение вещи передан несуществующий id вещи");
        }
        Item item = optionalItem.get();
        log.info("ItemService getById: выполнен запрос на получение вещи по id {}", itemId);
        return item;
    }

    @Override
    public Item create(final Item itemToCreate, final Long userId) {
        log.info("ItemService create: запрос на создание вещи {} с id пользователя {}", itemToCreate, userId);
        Optional<User> optionalUser = userRepository.getById(userId);
        if (optionalUser.isEmpty()) {
            String message = String.format("При запросе на создание вещи передан несуществующий id владельца %d", userId);
            throw new NotFoundException(message);
        }
        User owner = optionalUser.get();
        itemToCreate.setOwner(owner);
        Item createdItem = itemRepository.create(itemToCreate);
        log.info("ItemService create: выполнен запрос на создание вещи {} с id пользователя {}", createdItem, userId);
        return createdItem;
    }

    @Override
    public Item update(final Item newItem, final Long itemId, final Long userId) {
        log.info("ItemService update: запрос на обновление вещи с id {}", itemId);
        Optional<Item> optionalItem = itemRepository.getById(itemId);
        if (optionalItem.isEmpty()) {
            String message = String.format("При запросе на обновление вещи передан несуществующий id %d", itemId);
            throw new NotFoundException(message);
        }
        Item itemToUpdate = new Item(optionalItem.get());
        if (!Objects.equals(itemToUpdate.getOwner().getId(), userId)) {
            String message = String.format("При запросе на обновление вещи передан несоответствующий вещи с id %d " +
                    "пользователь с id %d", itemId, userId);
            throw new NotFoundException(message);
        }
        Boolean available = newItem.getAvailable();
        String name = newItem.getName();
        String description = newItem.getDescription();
        if (available != null) {
            itemToUpdate.setAvailable(available);
        }
        if (name != null) {
            itemToUpdate.setName(name);
        }
        if (description != null) {
            itemToUpdate.setDescription(description);
        }
        Item updatedItem = itemRepository.update(itemToUpdate);
        log.info("ItemService update: выполнен запрос на обновление вещи с id {}", itemId);
        return updatedItem;
    }

    @Override
    public List<Item> search(final String text) {
        log.info("ItemService search: запрос на поиск вещей по тексту \"{}\"", text);
        List<Item> items;
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        } else {
            String lowerCaseText = text.toLowerCase();
            items = itemRepository.search(lowerCaseText);
        }
        log.info("ItemService search: выполнен запрос на поиск вещей по тексту \"{}\"", text);
        return items;
    }
}
