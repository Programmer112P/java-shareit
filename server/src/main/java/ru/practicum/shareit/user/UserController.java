package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable(name = "userId") final Long userId) {
        log.info("UserController: запрос на получение пользователя с id {}", userId);
        User user = userService.getById(userId);
        UserDto response = userMapper.userToDto(user);
        log.info("UserController: выполнен запрос на получение пользователя с id {}", userId);
        return response;
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("UserController getAll: запрос на получение всех пользователей");
        List<User> users = userService.getAll();
        List<UserDto> response = userMapper.userListToDtoList(users);
        log.info("UserController getAll: запрос на получение всех пользователей");
        return response;
    }

    @PostMapping()
    public UserDto create(@RequestBody final CreateUserDto createUserDto) {
        log.info("UserController create: запрос на создание пользователя {}", createUserDto);
        User userToCreate = userMapper.createDtoToUser(createUserDto);
        User createdUser = userService.create(userToCreate);
        UserDto response = userMapper.userToDto(createdUser);
        log.info("UserController create: выполнен запрос на создание пользователя {}", response);
        return response;
    }

    @PatchMapping("/{userId}")
    public UserDto update(
            @RequestBody final UserDto userDto,
            @PathVariable(name = "userId") final Long userId) {
        log.info("UserController update: запрос на обновление пользователя с id {}", userId);
        User newUser = userMapper.dtoToUser(userDto);
        User updatedUser = userService.update(newUser, userId);
        UserDto response = userMapper.userToDto(updatedUser);
        log.info("UserController update: выполнен запрос на обновление пользователя с id {}", userId);
        return response;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable(name = "userId") final Long userId) {
        log.info("UserController: запрос на удаление пользователя с id {}", userId);
        userService.delete(userId);
        log.info("UserController: выполнен запрос на удаление пользователя с id {}", userId);
    }
}
