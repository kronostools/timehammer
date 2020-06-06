package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.ValidatePhase;
import com.kronostools.timehammer.common.messages.ValidationError;
import com.kronostools.timehammer.common.messages.constants.ValidateResult;

import java.util.List;

@JsonDeserialize(builder = ValidateRegistrationRequestPhaseBuilder.class)
public class ValidateRegistrationRequestPhase extends ValidatePhase {
    ValidateRegistrationRequestPhase(final ValidateResult result, final List<ValidationError> validationErrors) {
        super(result, validationErrors);
    }
}