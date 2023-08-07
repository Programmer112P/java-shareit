package ru.practicum.shareit.shared.errorhandler.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadRequestResponse {
    @JsonProperty("error")
    private final String errorMessage;
}
