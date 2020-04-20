package com.kronostools.timehammer.dto.form.validation;

import com.kronostools.timehammer.service.TimeMachineService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.LocalTime;
import java.util.regex.Pattern;

public class TimeIntervalDurationValidator implements ConstraintValidator<TimeIntervalDurationValid, String> {
    private static final Pattern timeInterval24hPattern = Pattern.compile("([01][0-9]|2[0-3]):[0-5][0-9];([01][0-9]|2[0-3]):[0-5][0-9]");

    private long min;
    private long max;

    @Override
    public void initialize(TimeIntervalDurationValid constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Boolean result = Boolean.TRUE;

        if (value != null) {
            final LocalTime start = TimeMachineService.getStartFromTimeInterval(value);
            final LocalTime end = TimeMachineService.getEndFromTimeInterval(value);
            final Long durationInMinutes = Duration.between(start, end).toMinutes();

            result = durationInMinutes >= min && durationInMinutes <= max;
        }

        return result;
    }
}