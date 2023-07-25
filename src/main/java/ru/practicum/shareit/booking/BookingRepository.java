package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Status;
import ru.practicum.shareit.shared.pagination.OffsetBasedPageRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByEndBeforeAndBookerAndStatusOrderByStartDesc(LocalDateTime now, User booker, Status status, OffsetBasedPageRequest pageable);

    List<Booking> findAllByStartAfterAndBookerAndStatusInOrderByStartDesc(LocalDateTime now, User booker, List<Status> statuses, OffsetBasedPageRequest pageable);

    List<Booking> findAllByStartBeforeAndEndAfterAndBookerOrderByStartDesc(LocalDateTime start, LocalDateTime end, User user, OffsetBasedPageRequest pageable);

    List<Booking> findAllByStatusAndBookerOrderByStartDesc(Status status, User booker, OffsetBasedPageRequest pageable);

    List<Booking> findAllByBookerOrderByStartDesc(User booker, OffsetBasedPageRequest pageable);

    List<Booking> findAllByItemInAndEndBeforeOrderByStartDesc(List<Item> items, LocalDateTime now, OffsetBasedPageRequest pageable);

    List<Booking> findAllByItemInAndStartAfterOrderByStartDesc(List<Item> items, LocalDateTime now, OffsetBasedPageRequest pageable);

    List<Booking> findAllByStartBeforeAndEndAfterAndItemInOrderByStartDesc(LocalDateTime start, LocalDateTime end, List<Item> items, OffsetBasedPageRequest pageable);

    List<Booking> findAllByStatusAndItemInOrderByStartDesc(Status status, List<Item> items, OffsetBasedPageRequest pageable);

    List<Booking> findAllByItemInOrderByStartDesc(List<Item> items, OffsetBasedPageRequest pageable);

    List<Booking> findByItemAndBooker(Item item, User booker);
}
