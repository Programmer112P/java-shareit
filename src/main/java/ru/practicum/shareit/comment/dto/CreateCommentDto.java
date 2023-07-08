package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateCommentDto {

    @NotBlank
    private String text;

    private LocalDateTime created;
}
