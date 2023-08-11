package ru.practicum.shareit.booking.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartAfterEndValidator.class)
@Documented
public @interface StartAfterEnd {

    String message() default "The start date after end date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
