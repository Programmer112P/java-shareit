package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoToUserMapper {

    User dtoToUser(CreateUserDto userDto);

    List<User> dtoListToUserList(List<CreateUserDto> userDtoList);
}
