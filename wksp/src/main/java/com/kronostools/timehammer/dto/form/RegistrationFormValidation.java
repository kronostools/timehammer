package com.kronostools.timehammer.dto.form;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public interface RegistrationFormValidation {
    @NotBlank
    String getRegistrationId();

    @NotBlank
    String getExternalId();

    @NotBlank
    String getExternalPassword();

    @NotBlank
    String getWorkCity();

    String getWorkSsid();

    @Size(min = 1)
    List<@Valid TimetableFormValidation> getTimetable();
}