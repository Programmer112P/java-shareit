package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemRequestResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ResponseItemRequestDto {

    private Long id;

    private String description;

    private LocalDateTime created;

    private List<ItemRequestResponseDto> items;
}
