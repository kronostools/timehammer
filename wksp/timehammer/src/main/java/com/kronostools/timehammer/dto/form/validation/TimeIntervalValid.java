package com.kronostools.timehammer.dto.form.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeIntervalValidator.class)
public @interface TimeIntervalValid {
    String message() default "{com.kronostools.timehammer.dto.form.validation.TimeIntervalValid.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        TimeIntervalValid[] value();
    }
}