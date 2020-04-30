package com.kronostools.timehammer.dto.form;

import com.kronostools.timehammer.dto.form.validation.RegistrationValidationGroup2;
import com.kronostools.timehammer.dto.form.validation.TimeInterval24hValid;
import com.kronostools.timehammer.dto.form.validation.TimeIntervalDurationValid;
import com.kronostools.timehammer.dto.form.validation.TimeIntervalValid;

import javax.validation.constraints.NotBlank;

public interface TimetableFormValidation {

    @NotBlank
    @TimeInterval24hValid
    @TimeIntervalValid(groups = RegistrationValidationGroup2.class)
    String getWorkTimeMon();

    @NotBlank
    @TimeInterval24hValid
    @TimeIntervalDurationValid(min = 15L, max = 90L, groups = RegistrationValidationGroup2.class)
    @TimeIntervalValid(groups = RegistrationValidationGroup2.class)
    String getLunchTimeMon();

    @NotBlank
    @TimeInterval24hValid
    @TimeIntervalValid(groups = RegistrationValidationGroup2.class)
    String getWorkTimeTue();

    @NotBlank
    @TimeInterval24hValid
    @TimeIntervalDurationValid(min = 15L, max = 90L, groups = RegistrationValidationGroup2.class)
    @TimeIntervalValid(groups = RegistrationValidationGroup2.class)
    String getLunchTimeTue();

    @NotBlank
    @TimeInterval24hValid
    @TimeIntervalValid(groups = RegistrationValidationGroup2.class)
    String getWorkTimeWed();

    @NotBlank
    @TimeInterval24hValid
    @TimeIntervalDurationValid(min = 15L, max = 90L, groups = RegistrationValidationGroup2.class)
    @TimeIntervalValid(groups = RegistrationValidationGroup2.class)
    String getLunchTimeWed();

    @NotBlank
    @TimeInterval24hValid
    @TimeIntervalValid(groups = RegistrationValidationGroup2.class)
    String getWorkTimeThu();

    @NotBlank
    @TimeInterval24hValid
    @TimeIntervalDurationValid(min = 15L, max = 90L, groups = RegistrationValidationGroup2.class)
    @TimeIntervalValid(groups = RegistrationValidationGroup2.class)
    String getLunchTimeThu();

    @NotBlank
    @TimeInterval24hValid
    @TimeIntervalValid(groups = RegistrationValidationGroup2.class)
    String getWorkTimeFri();

    @TimeInterval24hValid
    @TimeIntervalDurationValid(min = 15L, max = 90L, groups = RegistrationValidationGroup2.class)
    @TimeIntervalValid(groups = RegistrationValidationGroup2.class)
    String getLunchTimeFri();
}