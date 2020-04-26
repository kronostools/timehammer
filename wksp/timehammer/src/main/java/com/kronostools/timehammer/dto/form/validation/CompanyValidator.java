package com.kronostools.timehammer.dto.form.validation;

import com.kronostools.timehammer.service.CompanyService;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@ApplicationScoped
public class CompanyValidator implements ConstraintValidator<CompanyValid, String> {
    private final CompanyService companyService;

    public CompanyValidator(final CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public void initialize(CompanyValid constraintAnnotation) {}

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        Boolean result = Boolean.TRUE;

        if (code != null) {
            result = companyService.companyByCodeExists(code);
        }

        return result;
    }
}