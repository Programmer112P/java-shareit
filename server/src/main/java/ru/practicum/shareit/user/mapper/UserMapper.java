package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User createDtoToUser(CreateUserDto userDto);

    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);

    List<UserDto> userListToDtoList(List<User> userList);
}
