package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.shared.exception.AccessDeniedException;
import ru.practicum.shareit.booking.exception.BookingAlreadyApprovedException;
import ru.practicum.shareit.booking.exception.ItemUnavailableException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Status;
import ru.practicum.shareit.shared.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Booking create(Booking bookingToCreate) {
        Long itemId = bookingToCreate.getItem().getId();
        Optional<Item> item0 = itemRepository.findById(itemId);
        validateItem(item0);
        bookingToCreate.setItem(item0.get());
        Optional<User> booker0 = userRepository.findById(bookingToCreate.getBooker().getId());
        validateBooker(booker0, bookingToCreate.getItem().getOwner().getId());
        bookingToCreate.setBooker(booker0.get());
        return bookingRepository.save(bookingToCreate);
    }

    @Override
    @Transactional
    public Booking approve(Boolean approved, Long bookingId, Long ownerId) {
        Optional<Booking> booking0 = bookingRepository.findById(bookingId);
        if (booking0.isEmpty()) {
            throw new NotFoundException("При запросе на подтверждение брони передан несуществующий id");
        }
        Booking booking = booking0.get();
        if (!Objects.equals(booking.getItem().getOwner().getId(), ownerId)) {
            throw new NotFoundException("При запросе на подтверждение брони передан неверный id владельца");
        }
        if (booking.getStatus() != Status.WAITING) {
            throw new BookingAlreadyApprovedException("При запросе на подтверждение брони передана уже подтвержденная бронь");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return booking;
    }

    @Override
    public Booking getById(Long bookingId, Long userId) {
        Optional<Booking> booking0 = bookingRepository.findById(bookingId);
        if (booking0.isEmpty()) {
            throw new NotFoundException("При запросе на получение информации о брони передан неверный id");
        }
        Booking booking = booking0.get();
        if (!Objects.equals(booking.getBooker().getId(), userId) &&
                !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new AccessDeniedException("В запросе на получении информации о брони передан id пользователя без доступа");
        }
        return booking;
    }

    @Override
    public List<Booking> getAllBookingsOfUser(Long userId, State state) {
        Optional<User> user0 = userRepository.findById(userId);
        if (user0.isEmpty()) {
            throw new NotFoundException("В запросе на получение всех броней пользователей передан несуществующий id");
        }
        User booker = user0.get();
        switch (state) {
            case PAST:
                return bookingRepository.findAllByEndBeforeAndBookerAndStatusOrderByStartDesc(LocalDateTime.now(), booker, Status.APPROVED);
            case FUTURE:
                return bookingRepository.findAllByStartAfterAndBookerAndStatusInOrderByStartDesc(LocalDateTime.now(), booker, List.of(Status.APPROVED, Status.WAITING));
            case CURRENT:
                return bookingRepository.findAllByStartBeforeAndEndAfterAndBookerOrderByStartDesc(
                        LocalDateTime.now(), LocalDateTime.now(), booker);
            case WAITING:
                return bookingRepository.findAllByStatusAndBookerOrderByStartDesc(Status.WAITING, booker);
            case REJECTED:
                return bookingRepository.findAllByStatusAndBookerOrderByStartDesc(Status.REJECTED, booker);
            default:
                return bookingRepository.findAllByBookerOrderByStartDesc(booker);
        }
    }

    @Override
    public List<Booking> getAllBookingsOfUserItems(Long userId, State state) {
        Optional<User> user0 = userRepository.findById(userId);
        if (user0.isEmpty()) {
            throw new NotFoundException("В запросе на получение всех броней пользователей передан несуществующий id");
        }
        User booker = user0.get();
        if (booker.getItems().isEmpty()) {
            return Collections.emptyList();
        }
        List<Item> items = booker.getItems();
        switch (state) {
            case PAST:
                return bookingRepository.findAllByItemInAndEndBeforeOrderByStartDesc(items, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByItemInAndStartAfterOrderByStartDesc(items, LocalDateTime.now());
            case CURRENT:
                return bookingRepository.findAllByStartBeforeAndEndAfterAndItemInOrderByStartDesc(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        items);
            case WAITING:
                return bookingRepository.findAllByStatusAndItemInOrderByStartDesc(Status.WAITING, items);
            case REJECTED:
                return bookingRepository.findAllByStatusAndItemInOrderByStartDesc(Status.REJECTED, items);
            default:
                return bookingRepository.findAllByItemInOrderByStartDesc(items);
        }
    }

    private void validateBooker(Optional<User> booker0, Long ownerId) {
        if (booker0.isEmpty() || Objects.equals(booker0.get().getId(), ownerId)) {
            throw new NotFoundException("При запросе на создание брони передан несуществующий id пользователя");
        }
    }

    //Мне не нравится валидация с пробрасыванием исключений, но лучше я не придумал
    private void validateItem(Optional<Item> item0) {
        if (item0.isEmpty()) {
            throw new NotFoundException("При запросе на создание брони передан несуществующий id вещи");
        }
        Item item = item0.get();
        if (!item.getAvailable()) {
            throw new ItemUnavailableException("При запросе за создание брони передан вещь недоступна для бронирования");
        }
    }
}
