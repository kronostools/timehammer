package com.kronostools.timehammer.dto.form;

import com.kronostools.timehammer.dto.form.validation.CityValid;
import com.kronostools.timehammer.dto.form.validation.CompanyValid;
import com.kronostools.timehammer.enums.Company;
import com.kronostools.timehammer.vo.CityVo;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public interface RegistrationFormValidation {
    @NotBlank
    String getRegistrationId();

    @NotNull
    @CompanyValid
    Company getCompany();

    @NotBlank
    String getExternalId();

    @NotBlank
    String getExternalPassword();

    @NotNull
    @CityValid
    CityVo getWorkCity();

    String getWorkSsid();

    @Size(min = 1)
    List<@Valid TimetableFormValidation> getTimetable();
}