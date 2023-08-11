package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.exception.BookingAlreadyApprovedException;
import ru.practicum.shareit.booking.exception.ItemUnavailableException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Status;
import ru.practicum.shareit.shared.exception.AccessDeniedException;
import ru.practicum.shareit.shared.exception.NotFoundException;
import ru.practicum.shareit.shared.pagination.OffsetBasedPageRequest;
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
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        validateItem(itemOptional);
        bookingToCreate.setItem(itemOptional.get());
        Optional<User> bookerOptional = userRepository.findById(bookingToCreate.getBooker().getId());
        validateBooker(bookerOptional, bookingToCreate.getItem().getOwner().getId());
        bookingToCreate.setBooker(bookerOptional.get());
        return bookingRepository.save(bookingToCreate);
    }

    @Override
    @Transactional
    public Booking approve(Boolean approved, Long bookingId, Long ownerId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            throw new NotFoundException("При запросе на подтверждение брони передан несуществующий id");
        }
        Booking booking = bookingOptional.get();
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
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            throw new NotFoundException("При запросе на получение информации о брони передан неверный id");
        }
        Booking booking = bookingOptional.get();
        if (!Objects.equals(booking.getBooker().getId(), userId) &&
                !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new AccessDeniedException("В запросе на получении информации о брони передан id пользователя без доступа");
        }
        return booking;
    }

    @Override
    public List<Booking> getAllBookingsOfUser(Long userId, State state, Long from, Integer size) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("В запросе на получение всех броней пользователей передан несуществующий id");
        }
        User booker = userOptional.get();
        OffsetBasedPageRequest pageable = new OffsetBasedPageRequest(from, size);
        switch (state) {
            case PAST:
                return bookingRepository.findAllByEndBeforeAndBookerAndStatusOrderByStartDesc(LocalDateTime.now(), booker, Status.APPROVED, pageable);
            case FUTURE:
                return bookingRepository.findAllByStartAfterAndBookerAndStatusInOrderByStartDesc(LocalDateTime.now(), booker, List.of(Status.APPROVED, Status.WAITING), pageable);
            case CURRENT:
                return bookingRepository.findAllByStartBeforeAndEndAfterAndBookerOrderByStartDesc(
                        LocalDateTime.now(), LocalDateTime.now(), booker, pageable);
            case WAITING:
                return bookingRepository.findAllByStatusAndBookerOrderByStartDesc(Status.WAITING, booker, pageable);
            case REJECTED:
                return bookingRepository.findAllByStatusAndBookerOrderByStartDesc(Status.REJECTED, booker, pageable);
            default:
                return bookingRepository.findAllByBookerOrderByStartDesc(booker, pageable);
        }
    }

    @Override
    public List<Booking> getAllBookingsOfUserItems(Long userId, State state, Long from, Integer size) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("В запросе на получение всех броней пользователей передан несуществующий id");
        }
        User booker = userOptional.get();
        if (booker.getItems().isEmpty()) {
            return Collections.emptyList();
        }
        List<Item> items = booker.getItems();
        OffsetBasedPageRequest pageable = new OffsetBasedPageRequest(from, size);
        switch (state) {
            case PAST:
                return bookingRepository.findAllByItemInAndEndBeforeOrderByStartDesc(items, LocalDateTime.now(), pageable);
            case FUTURE:
                return bookingRepository.findAllByItemInAndStartAfterOrderByStartDesc(items, LocalDateTime.now(), pageable);
            case CURRENT:
                return bookingRepository.findAllByStartBeforeAndEndAfterAndItemInOrderByStartDesc(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        items,
                        pageable);
            case WAITING:
                return bookingRepository.findAllByStatusAndItemInOrderByStartDesc(Status.WAITING, items, pageable);
            case REJECTED:
                return bookingRepository.findAllByStatusAndItemInOrderByStartDesc(Status.REJECTED, items, pageable);
            default:
                return bookingRepository.findAllByItemInOrderByStartDesc(items, pageable);
        }
    }

    private void validateBooker(Optional<User> bookerOptional, Long ownerId) {
        if (bookerOptional.isEmpty() || Objects.equals(bookerOptional.get().getId(), ownerId)) {
            throw new NotFoundException("При запросе на создание брони передан несуществующий id пользователя");
        }
    }

    private void validateItem(Optional<Item> itemOptional) {
        if (itemOptional.isEmpty()) {
            throw new NotFoundException("При запросе на создание брони передан несуществующий id вещи");
        }
        Item item = itemOptional.get();
        if (!item.getAvailable()) {
            throw new ItemUnavailableException("При запросе за создание брони передан вещь недоступна для бронирования");
        }
    }
}
