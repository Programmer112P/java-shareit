package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.exception.CommentDeniedException;
import ru.practicum.shareit.comment.model.Comment;
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
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository repository, UserRepository userRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = repository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public List<Item> getAllByUserId(final Long userId) {
        log.info("ItemService getAllByUserId: запрос на получение всех вещей пользователя с id {}", userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("При получении списка вещей передан несуществующий id пользователя");
        }
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        for (Item item : items) {
            getBookingsOfItem(item);
        }
        log.info("ItemService getAllByUserId: запрос на получение всех вещей пользователя с id {}", userId);
        return items;
    }

    @Override
    public Item getById(final Long itemId, final Long userId) {
        log.info("ItemService getById: запрос на получение вещи по id {}", itemId);
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new NotFoundException("При запросе на получение вещи передан несуществующий id вещи");
        }
        Item item = optionalItem.get();
        if (Objects.equals(item.getOwner().getId(), userId)) {
            getBookingsOfItem(item);
        }
        log.info("ItemService getById: выполнен запрос на получение вещи по id {}", itemId);
        return item;
    }

    private Item getBookingsOfItem(Item item) {
        List<Booking> bookings = item.getBookings();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last = LocalDateTime.MIN;
        LocalDateTime next = LocalDateTime.MAX;
        Booking nextBooking = null;
        Booking lastBooking = null;
        for (Booking booking : bookings) {
            if (booking.getStart().isAfter(now) && booking.getStart().isBefore(next) && booking.getStatus().equals(Status.APPROVED)) {
                next = booking.getStart();
                nextBooking = booking;
            } else if (booking.getStart().isBefore(now) && booking.getStart().isAfter(last) && booking.getStatus().equals(Status.APPROVED)) {
                last = booking.getStart();
                lastBooking = booking;
            }
        }
        if (lastBooking == null) {
            lastBooking = nextBooking;
        }
        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);
        return item;
    }

    @Override
    public Item create(final Item itemToCreate, final Long userId) {
        log.info("ItemService create: запрос на создание вещи {} с id пользователя {}", itemToCreate, userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            String message = String.format("При запросе на создание вещи передан несуществующий id владельца %d", userId);
            throw new NotFoundException(message);
        }
        User owner = optionalUser.get();
        itemToCreate.setOwner(owner);
        Item createdItem = itemRepository.save(itemToCreate);
        log.info("ItemService create: выполнен запрос на создание вещи {} с id пользователя {}", createdItem, userId);
        return createdItem;
    }

    @Override
    public Item update(final Item newItem, final Long itemId, final Long userId) {
        log.info("ItemService update: запрос на обновление вещи с id {}", itemId);
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            String message = String.format("При запросе на обновление вещи передан несуществующий id %d", itemId);
            throw new NotFoundException(message);
        }
        Item itemToUpdate = new Item(optionalItem.get());
        if (!Objects.equals(itemToUpdate.getOwner().getId(), userId)) {
            String message = String.format("При запросе на обновление вещи передан несоответствующий вещи с id %d " +
                    "пользователь с id %d", itemId, userId);
            throw new NotFoundException(message);
        }
        Boolean available = newItem.getAvailable();
        String name = newItem.getName();
        String description = newItem.getDescription();
        if (available != null) {
            itemToUpdate.setAvailable(available);
        }
        if (name != null) {
            itemToUpdate.setName(name);
        }
        if (description != null) {
            itemToUpdate.setDescription(description);
        }
        Item updatedItem = itemRepository.save(itemToUpdate);
        log.info("ItemService update: выполнен запрос на обновление вещи с id {}", itemId);
        return updatedItem;
    }

    @Override
    public List<Item> search(final String text) {
        log.info("ItemService search: запрос на поиск вещей по тексту \"{}\"", text);
        List<Item> items;
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        } else {
            String lowerCaseText = text.toLowerCase();
            items = itemRepository.search(lowerCaseText);
        }
        log.info("ItemService search: выполнен запрос на поиск вещей по тексту \"{}\"", text);
        return items;
    }

    @Override
    @Transactional
    public Comment addComment(Long userId, Long itemId, Comment comment) {
        Optional<User> user0 = userRepository.findById(userId);
        if (user0.isEmpty()) {
            throw new NotFoundException("При запросе на оставления комментария передан несуществующий id пользователя");
        }
        User user = user0.get();
        Optional<Item> item0 = itemRepository.findById(itemId);
        if (item0.isEmpty()) {
            throw new NotFoundException("При запросе на оставления комментария передан несуществующий id вещи");
        }
        Item item = item0.get();
        List<Booking> bookings = bookingRepository.findByItemAndBooker(item, user);
        LocalDateTime now = LocalDateTime.now();
        if (bookings.isEmpty() || bookings.stream().map(Booking::getStatus).anyMatch(b -> !b.equals(Status.APPROVED))
                || bookings.stream().map(Booking::getStart).allMatch(start -> start.isAfter(now))) {
            throw new CommentDeniedException("Запрос на оставления комментария отправлен от пользователе, не бравшего вещь в аренду");
        }
        comment.setItem(item);
        comment.setAuthor(user);
        Comment created = commentRepository.save(comment);
        return created;
    }
}
