package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.shared.exception.NotFoundException;
import ru.practicum.shareit.shared.pagination.OffsetBasedPageRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Comparator;
import java.util.List;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Autowired
    public ItemRequestServiceImpl(UserRepository userRepository, ItemRequestRepository itemRequestRepository) {
        this.userRepository = userRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Override
    @Transactional
    public ItemRequest create(ItemRequest itemRequest, Long userId) {
        User requestor = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("При запросе на создание itemRequest не найден пользователь");
        });
        //Сетаю, потому что надо проверить существование пользователя. Иначе бизнес-логика была бы в маппере
        itemRequest.setRequestor(requestor);
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public List<ItemRequest> getByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("При запросе на получение всех itemRequest не найден пользователь");
        });
        List<ItemRequest> result = user.getItemRequests();
        //либо 2 раза идти в БД, либо сортить тут силами java
        result.sort(Comparator.comparing(ItemRequest::getCreated).reversed());
        return result;
    }

    @Override
    public ItemRequest getById(Long requestId, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("При запросе на получение itemRequest передан несуществующий пользователь");
        });
        return itemRequestRepository.findById(requestId).orElseThrow(() -> {
            throw new NotFoundException("При запросе на получение itemRequest передан несуществующий id запроса");
        });
    }

    @Override
    public List<ItemRequest> getPage(long from, int size, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("При запросе на получение страницы передан несуществующий id пользователя");
        });
        OffsetBasedPageRequest pageable = new OffsetBasedPageRequest(from, size, Sort.by("created").descending());
        return itemRequestRepository.findAllByIdNotIn(List.of(userId), pageable);
    }
}
