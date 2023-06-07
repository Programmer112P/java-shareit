package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class FakeUserRepository {

    private final Map<Long, User> repository = new HashMap<>();
    private long idGenerator = 1;

    public Optional<User> getById(final Long userId) {
        User user = repository.get(userId);
        if (user == null) {
            return Optional.empty();
        } else {
            return Optional.of(user);
        }
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
        userToCreate.setId(idGenerator++);
        repository.put(userToCreate.getId(), userToCreate);
        return Optional.of(userToCreate);
    }

    public User update(User userToUpdate, Long userId) {
        return repository.put(userId, userToUpdate);
    }

    public Optional<User> delete(Long userId) {
        User deletedUser = repository.remove(userId);
        if (deletedUser == null) {
            return Optional.empty();
        }
        return Optional.of(deletedUser);
    }
}
