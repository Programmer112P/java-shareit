package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable(name = "userId") final Long userId) {
        log.info("UserController: запрос на получение пользователя с id {}", userId);
        ResponseEntity<Object> response = userClient.getById(userId);
        log.info("UserController: выполнен запрос на получение пользователя с id {}", userId);
        return response;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("UserController getAll: запрос на получение всех пользователей");
        ResponseEntity<Object> response = userClient.getAll();
        log.info("UserController getAll: запрос на получение всех пользователей");
        return response;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid final CreateUserDto createUserDto) {
        log.info("UserController create: запрос на создание пользователя {}", createUserDto);
        ResponseEntity<Object> response = userClient.create(createUserDto);
        log.info("UserController create: выполнен запрос на создание пользователя {}", response);
        return response;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(
            @RequestBody final UserDto userDto,
            @PathVariable(name = "userId") final Long userId) {
        log.info("UserController update: запрос на обновление пользователя с id {}", userId);
        ResponseEntity<Object> response = userClient.update(userDto, userId);
        log.info("UserController update: выполнен запрос на обновление пользователя с id {}", userId);
        return response;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "userId") final Long userId) {
        log.info("UserController: запрос на удаление пользователя с id {}", userId);
        ResponseEntity<Object> response = userClient.deleteUser(userId);
        log.info("UserController: выполнен запрос на удаление пользователя с id {}", userId);
        return response;
    }
}
