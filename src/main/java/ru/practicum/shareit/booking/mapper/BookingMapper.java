package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "booker",
            expression = "java(bookerFromId(createBookingDto.getBookerId()))")
    @Mapping(target = "item",
            expression = "java(itemFromId(createBookingDto.getItemId()))")
    Booking createDtoToModel(CreateBookingDto createBookingDto);

    List<Booking> createDtoListToModelList(List<CreateBookingDto> createBookingDtoList);

    @Mapping(target = "booker",
            expression = "java(userDtoFromEntity(booking.getBooker()))")
    @Mapping(target = "item",
            expression = "java(itemDtoFromEntity(booking.getItem()))")
    BookingDto modelToDto(Booking booking);

    List<BookingDto> modelListToDtoList(List<Booking> bookingList);

    default User bookerFromId(Long userId) {
        return new User(userId);
    }

    default Item itemFromId(Long itemId) {
        return new Item(itemId);
    }

    default UserDto userDtoFromEntity(User booker) {
        return new UserDto(booker.getId(), booker.getName(), booker.getEmail());
    }

    default ItemDto itemDtoFromEntity(Item item) {
        return new ItemDto(item.getId(), item.getName());
    }
}