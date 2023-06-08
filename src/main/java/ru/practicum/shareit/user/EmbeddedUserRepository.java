package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class EmbeddedUserRepository {

    private final Map<Long, User> repository = new HashMap<>();
    private long idGenerator = 0;

    public Optional<User> getById(final Long userId) {
        User user = repository.get(userId);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public List<User> getAll() {
        return new ArrayList<>(repository.values());
    }

    public Optional<User> create(User userToCreate) {
        boolean isEmailAlreadyOccupied = repository.values()
                .stream()
                .anyMatch(user -> user.getEmail().equals(userToCreate.getEmail()));
        if (isEmailAlreadyOccupied) {
            return Optional.empty();
        }
        userToCreate.setId(++idGenerator);
        repository.put(userToCreate.getId(), userToCreate);
        return Optional.of(userToCreate);
    }

    public Optional<User> update(User userToUpdate) {
        for (Map.Entry<Long, User> entry : repository.entrySet()) {
            if (!entry.getKey().equals(userToUpdate.getId()) &&
                    entry.getValue().getEmail().equals(userToUpdate.getEmail())) {
                return Optional.empty();
            }
        }
        repository.put(userToUpdate.getId(), userToUpdate);
        return Optional.of(userToUpdate);
    }

    public void delete(Long userId) {
        repository.remove(userId);
    }
}
