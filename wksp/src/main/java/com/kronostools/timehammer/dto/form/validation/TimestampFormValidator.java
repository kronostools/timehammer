package com.kronostools.timehammer.dto.form.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DateTimeException;
import java.time.LocalDateTime;

public class TimestampFormValidator implements ConstraintValidator<TimestampFormValid, TimestampForm> {
    @Override
    public void initialize(final TimestampFormValid constraintAnnotation) {}

    @Override
    public boolean isValid(final TimestampForm value, final ConstraintValidatorContext context) {
        Boolean result = Boolean.TRUE;

        try {
            LocalDateTime.of(value.getYear(), value.getMonth(), value.getDay(), value.getHours(), value.getMinutes());
        } catch (DateTimeException e) {
            result = Boolean.FALSE;
        }

        return result;
    }
}