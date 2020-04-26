package com.kronostools.timehammer.dto.form.validation;

import com.kronostools.timehammer.service.CityService;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@ApplicationScoped
public class CityValidator implements ConstraintValidator<CityValid, String> {
    private final CityService cityService;

    public CityValidator(final CityService cityService) {
        this.cityService = cityService;
    }

    @Override
    public void initialize(CityValid constraintAnnotation) {}

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        Boolean result = Boolean.TRUE;

        if (code != null) {
            result = cityService.cityByCodeExists(code);
        }

        return result;
    }
}