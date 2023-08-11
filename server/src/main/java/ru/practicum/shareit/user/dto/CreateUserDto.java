package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateUserDto {

    private String name;

    private String email;

}
