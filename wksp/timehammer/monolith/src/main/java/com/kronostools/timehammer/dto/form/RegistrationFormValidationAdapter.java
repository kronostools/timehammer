package com.kronostools.timehammer.dto.form;

import com.kronostools.timehammer.dto.RegistrationForm;
import com.kronostools.timehammer.enums.Company;
import com.kronostools.timehammer.vo.CityVo;

import java.util.List;
import java.util.stream.Collectors;

public class RegistrationFormValidationAdapter implements RegistrationFormValidation {
    private final RegistrationForm registrationFormSubmit;

    public RegistrationFormValidationAdapter(final RegistrationForm registrationFormSubmit) {
        this.registrationFormSubmit = registrationFormSubmit;
    }

    @Override
    public String getInternalId() {
        return registrationFormSubmit.getInternalId();
    }

    @Override
    public Company getCompany() {
        return registrationFormSubmit.getCompany();
    }

    @Override
    public String getExternalId() {
        return registrationFormSubmit.getExternalId();
    }

    @Override
    public String getExternalPassword() {
        return registrationFormSubmit.getExternalPassword();
    }

    @Override
    public CityVo getWorkCity() {
        return registrationFormSubmit.getWorkCity();
    }

    @Override
    public String getWorkSsid() {
        return registrationFormSubmit.getWorkSsid();
    }

    @Override
    public List<TimetableFormValidation> getTimetable() {
        return registrationFormSubmit.getTimetables()
                .stream()
                .map(TimetableFormValidationAdapter::new)
                .collect(Collectors.toList());
    }
}