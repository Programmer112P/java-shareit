package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    ItemServiceImpl itemService;

    @Test
    void getById_shouldReturnCorrectItem_whenValidRequest() {
        User owner = User.builder()
                .id(5L)
                .build();
        Booking nextBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(2))
                .status(Status.APPROVED)
                .build();
        Booking lastBooking = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().minusDays(2))
                .status(Status.APPROVED)
                .build();
        Booking futureBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(4))
                .status(Status.APPROVED)
                .build();
        Booking pastBooking = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().minusDays(4))
                .status(Status.APPROVED)
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("item_name")
                .available(true)
                .description("description")
                .bookings(List.of(lastBooking, nextBooking, futureBooking, pastBooking))
                .owner(owner)
                .build();
        doReturn(Optional.of(item)).when(itemRepository).findById(anyLong());

        Item actual = itemService.getById(1L, 5L);
        assertThat(actual.getLastBooking()).isEqualTo(lastBooking);
        assertThat(actual.getNextBooking()).isEqualTo(nextBooking);
    }
}