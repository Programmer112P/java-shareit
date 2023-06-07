package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.shared.exception.ConflictException;
import ru.practicum.shareit.shared.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final FakeUserRepository repository;

    @Autowired
    public UserServiceImpl(FakeUserRepository repository) {
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
        User userToUpdate = optionalUser.get();
        String newName = newUser.getName();
        String newEmail = newUser.getEmail();
        if (newEmail != null) {
            boolean isEmailOccupied = validateEmail(newEmail, userId);
            if (isEmailOccupied) {
                String message = String.format(
                        "При запросе на обновление пользователя передан уже занятый email %s", newEmail);
                throw new ConflictException(message);
            }
            userToUpdate.setEmail(newEmail);
        }
        if (newName != null) {
            userToUpdate.setName(newName);
        }
        User updatedUser = repository.update(userToUpdate, userId);
        log.info("UserService update: выполнен запрос на обновление пользователя с id {}", userId);
        return updatedUser;
    }

    @Override
    public void delete(Long userId) {
        log.info("UserService delete: запрос на удаление пользователя с id {}", userId);
        Optional<User> deletedUser = repository.delete(userId);
        if (deletedUser.isEmpty()) {
            String message = String.format("При удалении пользователя передан несуществующий id %d", userId);
            throw new NotFoundException(message);
        }
    }

    //По идее можно настроить базу данных так, чтобы она сама не пуская дупликаты (сделать Unique)
    //Но здесь приходится делать валидацию руками
    private boolean validateEmail(String email, Long userId) {
        List<User> users = getAll();
        return users
                .stream()
                .anyMatch(user -> user.getEmail().equals(email) && !Objects.equals(user.getId(), userId));
    }
}
