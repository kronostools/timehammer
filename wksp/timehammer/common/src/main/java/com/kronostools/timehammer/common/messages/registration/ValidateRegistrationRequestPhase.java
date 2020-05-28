package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

@JsonDeserialize(builder = CheckRegistrationRequestPhaseBuilder.class)
public class ValidateRegistrationRequestPhase extends Phase<SimpleResult> {
    ValidateRegistrationRequestPhase(final SimpleResult result, final String errorMessage) {
        super(result, errorMessage);
    }
}