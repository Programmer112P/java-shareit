package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.dto.CreateBookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartAfterEndValidator implements ConstraintValidator<StartAfterEnd, CreateBookingDto> {

    @Override
    public void initialize(StartAfterEnd constraintAnnotation) {
    }

    @Override
    public boolean isValid(CreateBookingDto createBookingDto, ConstraintValidatorContext constraintValidatorContext) {
        if (createBookingDto.getEnd() == null || createBookingDto.getStart() == null) {
            return false;
        }
        return !createBookingDto.getEnd().isBefore(createBookingDto.getStart()) && !createBookingDto.getEnd().equals(createBookingDto.getStart());
    }
}
