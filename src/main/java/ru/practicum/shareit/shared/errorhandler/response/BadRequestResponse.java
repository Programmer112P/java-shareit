package ru.practicum.shareit.shared.errorhandler.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadRequestResponse {
    private final String description;
}
