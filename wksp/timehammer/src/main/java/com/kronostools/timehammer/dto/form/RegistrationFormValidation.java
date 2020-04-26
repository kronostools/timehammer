package com.kronostools.timehammer.dto.form;

import com.kronostools.timehammer.dto.form.validation.CityValid;
import com.kronostools.timehammer.dto.form.validation.CompanyValid;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public interface RegistrationFormValidation {
    @NotBlank
    String getRegistrationId();

    @NotBlank
    @CompanyValid
    String getCompany();

    @NotBlank
    String getExternalId();

    @NotBlank
    String getExternalPassword();

    @NotBlank
    @CityValid
    String getWorkCity();

    String getWorkSsid();

    @Size(min = 1)
    List<@Valid TimetableFormValidation> getTimetable();
}