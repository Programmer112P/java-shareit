package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserToDtoMapper {

    UserDto userToDto(User user);

    List<UserDto> userListToDtoList(List<User> userList);
}
