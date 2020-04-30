package com.kronostools.timehammer.dto.form.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeIntervalDurationValidator.class)
public @interface TimeIntervalDurationValid {
    String message() default "{com.kronostools.timehammer.dto.form.validation.TimeIntervalDurationValid.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    long min() default 0L;

    long max() default Long.MAX_VALUE;

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        TimeIntervalDurationValid[] value();
    }
}