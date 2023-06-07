package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.DtoToUserMapper;
import ru.practicum.shareit.user.mapper.UserToDtoMapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final UserToDtoMapper userToDtoMapper;
    private final DtoToUserMapper dtoToUserMapper;

    @Autowired
    public UserController(UserService userService, UserToDtoMapper userToDtoMapper, DtoToUserMapper dtoToUserMapper) {
        this.userService = userService;
        this.userToDtoMapper = userToDtoMapper;
        this.dtoToUserMapper = dtoToUserMapper;
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable(name = "userId") final Long userId) {
        log.info("UserController: запрос на получение пользователя с id {}", userId);
        User user = userService.getById(userId);
        UserDto response = userToDtoMapper.userToDto(user);
        log.info("UserController: выполнен запрос на получение пользователя с id {}", userId);
        return response;
    }

    //сделал бы пагинацию, но тесты не пройдет
    @GetMapping
    public List<UserDto> getAll() {
        log.info("UserController getAll: запрос на получение всех пользователей");
        List<User> users = userService.getAll();
        List<UserDto> response = userToDtoMapper.userListToDtoList(users);
        log.info("UserController getAll: запрос на получение всех пользователей");
        return response;
    }

    @PostMapping()
    public UserDto create(@RequestBody @Valid final CreateUserDto createUserDto) {
        log.info("UserController create: запрос на создание пользователя {}", createUserDto);
        User userToCreate = dtoToUserMapper.dtoToUser(createUserDto);
        User createdUser = userService.create(userToCreate);
        UserDto response = userToDtoMapper.userToDto(createdUser);
        log.info("UserController create: выполнен запрос на создание пользователя {}", response);
        return response;
    }

    @PatchMapping("/{userId}")
    public UserDto update(
            @RequestBody final CreateUserDto createUserDto,
            @PathVariable(name = "userId") final Long userId) {
        log.info("UserController update: запрос на обновление пользователя с id {}", userId);
        User newUser = dtoToUserMapper.dtoToUser(createUserDto);
        User updatedUser = userService.update(newUser, userId);
        UserDto response = userToDtoMapper.userToDto(updatedUser);
        log.info("UserController update: выполнен запрос на обновление пользователя с id {}", userId);
        return response;
    }

    //Возвращать что-нибудь при удалении?
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable(name = "userId") final Long userId) {
        log.info("UserController: запрос на удаление пользователя с id {}", userId);
        userService.delete(userId);
        log.info("UserController: выполнен запрос на удаление пользователя с id {}", userId);
    }
}
