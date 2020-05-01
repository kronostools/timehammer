package com.kronostools.timehammer.dto.form.validation;

import com.kronostools.timehammer.service.CityService;
import com.kronostools.timehammer.vo.CityVo;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@ApplicationScoped
public class CityValidator implements ConstraintValidator<CityValid, CityVo> {
    private final CityService cityService;

    public CityValidator(final CityService cityService) {
        this.cityService = cityService;
    }

    @Override
    public void initialize(CityValid constraintAnnotation) {}

    @Override
    public boolean isValid(CityVo city, ConstraintValidatorContext context) {
        Boolean result = Boolean.TRUE;

        if (city != null) {
            result = city.isValid();
        }

        return result;
    }
}