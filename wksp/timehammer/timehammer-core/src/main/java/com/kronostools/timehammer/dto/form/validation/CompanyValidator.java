package com.kronostools.timehammer.dto.form.validation;

import com.kronostools.timehammer.enums.Company;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CompanyValidator implements ConstraintValidator<CompanyValid, Company> {

    @Override
    public void initialize(CompanyValid constraintAnnotation) {}

    @Override
    public boolean isValid(Company company, ConstraintValidatorContext context) {
        Boolean result = Boolean.TRUE;

        if (company != null) {
            result = company != Company.UNKNOWN;
        }

        return result;
    }
}