package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.ValidatePhase;
import com.kronostools.timehammer.common.messages.ValidationError;
import com.kronostools.timehammer.common.messages.constants.SimpleValidateResult;
import com.kronostools.timehammer.common.messages.registration.forms.RegistrationRequestFormValidated;

import java.util.List;

@JsonDeserialize(builder = CheckRegistrationRequestPhaseBuilder.class)
public class ValidateRegistrationRequestPhase extends ValidatePhase<SimpleValidateResult, RegistrationRequestFormValidated> {
    ValidateRegistrationRequestPhase(final SimpleValidateResult result, final RegistrationRequestFormValidated validateForm, final List<ValidationError> validationErrors) {
        super(result, validateForm, validationErrors);
    }
}