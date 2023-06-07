package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.shared.exception.NotFoundException;
import ru.practicum.shareit.user.FakeUserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final FakeItemRepository itemRepository;
    private final FakeUserRepository userRepository;

    @Autowired
    public ItemServiceImpl(FakeItemRepository repository, FakeUserRepository userRepository) {
        this.itemRepository = repository;
        this.userRepository = userRepository;
    }

    /*
     * Мне нужно проверить существование пользователя, и только после этого достать все items
     * Я так и не понял как правильно обработать эту ситуацию
     * У меня 2 сущности завязаны друг на друга, и мне нужно вызывать чужой репозиторий
     * Я может слишком далеко заглядываю, но если когда-нибудь мне надо будет писать микросервисы,
     * то чужой репозиторий просто будет недоступен, так как это будут 2 отдельных приложения
     * И плюс к этому надо делать 2 запроса к БД, сначала на проверку, и только потом на получение данных
     * Даже если допустим сделать нативный SQL, а не JPA, все равно я не понимаю как мне в один запрос это поместить
     * */
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

    /*
     * Та же история, приходится ходить дважды к БД, чтобы все проверить
     * Вопрос есть сразу на будущее с JPA
     * Если у меня будет manyToOne связь, можно ли ещё хранить в Item просто OwnerId для сохранения в БД
     * А поле Owner будет только для чтения, если нужны будут ещё какие-то данные о владельце
     * */
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
        Item itemToUpdate = optionalItem.get();
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
        Item updatedItem = itemRepository.update(itemId, itemToUpdate);
        log.info("ItemService update: выполнен запрос на обновление вещи с id {}", itemId);
        return updatedItem;
    }

    @Override
    public List<Item> search(final String text) {
        log.info("ItemService search: запрос на поиск вещей по тексту \"{}\"", text);
        List<Item> items;
        if (!text.isBlank()) {
            String lowerCaseText = text.toLowerCase();
            items = itemRepository.search(lowerCaseText);
        } else {
            items = new ArrayList<>();
        }
        log.info("ItemService search: выполнен запрос на поиск вещей по тексту \"{}\"", text);
        return items;
    }
}
