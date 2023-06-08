package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class EmbeddedItemRepository {

    private final Map<Long, Item> repository = new HashMap<>();
    private long idGenerator = 0;

    public List<Item> getAllByUserId(Long userId) {
        return repository.values()
                .stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), userId))
                .collect(Collectors.toList());
    }

    public Optional<Item> getById(Long id) {
        Item item = repository.get(id);
        if (item == null) {
            return Optional.empty();
        } else {
            return Optional.of(item);
        }
    }

    public Item create(Item itemToCreate) {
        itemToCreate.setId(++idGenerator);
        repository.put(itemToCreate.getId(), itemToCreate);
        return itemToCreate;
    }

    public Item update(Item itemToUpdate) {
        repository.put(itemToUpdate.getId(), itemToUpdate);
        return repository.get(itemToUpdate.getId());
    }

    public List<Item> search(String text) {
        List<Item> result = new ArrayList<>();
        for (Item item : repository.values()) {
            String name = item.getName().toLowerCase();
            String description = item.getDescription().toLowerCase();
            if ((name.contains(text) || description.contains(text)) && item.getAvailable()) {
                result.add(item);
            }
        }
        return result;
    }
}
