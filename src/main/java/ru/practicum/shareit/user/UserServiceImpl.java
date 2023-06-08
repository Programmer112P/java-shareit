package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.shared.exception.ConflictException;
import ru.practicum.shareit.shared.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final EmbeddedUserRepository repository;

    @Autowired
    public UserServiceImpl(EmbeddedUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User getById(final Long userId) {
        log.info("UserService getById: запрос на получение пользователя с id {}", userId);
        Optional<User> optionalUser = repository.getById(userId);
        if (optionalUser.isEmpty()) {
            String message = String.format("В запросе на получение пользователя передан несуществующий id %d", userId);
            throw new NotFoundException(message);
        }
        User user = optionalUser.get();
        log.info("UserService getById: выполнен запрос на получение пользователя с id {}", userId);
        return user;
    }

    @Override
    public List<User> getAll() {
        log.info("UserService getAll: запрос на получение всех пользователей");
        List<User> users = repository.getAll();
        log.info("UserService getAll: выполнен запрос на получение всех пользователей");
        return users;
    }

    @Override
    public User create(final User userToCreate) {
        log.info("UserService create: запрос на создание пользователя {}", userToCreate);
        Optional<User> optionalUser = repository.create(userToCreate);
        if (optionalUser.isEmpty()) {
            String message = String.format(
                    "При создании пользователя передан уже занятый email %s", userToCreate.getEmail());
            throw new ConflictException(message);
        }
        User createdUser = optionalUser.get();
        log.info("UserService create: выполнен запрос на создание пользователя {}", createdUser);
        return createdUser;
    }

    @Override
    public User update(final User newUser, final Long userId) {
        log.info("UserService update: запрос на обновление пользователя с id {}", userId);
        Optional<User> optionalUser = repository.getById(userId);
        if (optionalUser.isEmpty()) {
            String message = String.format("В запросе на обновление пользователя задан несуществующий id %d", userId);
            throw new NotFoundException(message);
        }
        User userToUpdate = new User(optionalUser.get());
        String newName = newUser.getName();
        String newEmail = newUser.getEmail();
        if (newName != null) {
            userToUpdate.setName(newName);
        }
        if (newEmail != null) {
            userToUpdate.setEmail(newEmail);
        }
        Optional<User> updatedUser = repository.update(userToUpdate);
        if (updatedUser.isEmpty()) {
            String message = String.format("В запросе на обновление пользователя передан занятый email %s", newEmail);
            throw new ConflictException(message);
        }
        log.info("UserService update: выполнен запрос на обновление пользователя с id {}", userId);
        return updatedUser.get();
    }

    /*
     * Мне не нравится концептуально возвращать 200, когда передан несуществующий id
     * Это значит, что где-то в коде есть ошибка, но мы просто закрываем на эту возможность глаза
     * Поэтому я сначала проверяю есть ли такой user вообще
     * */
    @Override
    public void delete(Long userId) {
        log.info("UserService delete: запрос на удаление пользователя с id {}", userId);
        Optional<User> optionalUser = repository.getById(userId);
        if (optionalUser.isEmpty()) {
            String message = String.format("В запросе на удаление пользователя передан несуществующий id %d", userId);
            throw new NotFoundException(message);
        }
        repository.delete(userId);
    }
}
