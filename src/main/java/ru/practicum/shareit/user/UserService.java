package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User getById(Long userId);

    List<User> getAll();

    User create(User userToCreate);

    User update(User newUser, Long userId);

    void delete(Long userId);
}
