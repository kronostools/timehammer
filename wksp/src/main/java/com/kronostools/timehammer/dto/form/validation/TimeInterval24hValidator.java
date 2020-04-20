package com.kronostools.timehammer.dto.form.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class TimeInterval24hValidator implements ConstraintValidator<TimeInterval24hValid, String> {
    private static final Pattern timeInterval24hPattern = Pattern.compile("([01][0-9]|2[0-3]):[0-5][0-9];([01][0-9]|2[0-3]):[0-5][0-9]");

    @Override
    public void initialize(TimeInterval24hValid constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Boolean result = Boolean.TRUE;

        if (value != null) {
            result = timeInterval24hPattern.matcher(value).matches();
        }

        return result;
    }
}