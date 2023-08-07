package ru.practicum.shareit.shared.errorhandler.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ConflictResponse {
    private final String description;
}

