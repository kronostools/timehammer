package com.kronostools.timehammer.dto.form.validation;

import com.kronostools.timehammer.service.TimeMachineService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class TimeIntervalValidator implements ConstraintValidator<TimeIntervalValid, String> {
    @Override
    public void initialize(TimeIntervalValid constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Boolean result = Boolean.TRUE;

        if (value != null) {
            final LocalTime start = TimeMachineService.getStartFromTimeInterval(value);
            final LocalTime end = TimeMachineService.getEndFromTimeInterval(value);

            result = end.compareTo(start) > 0;
        }

        return result;
    }
}