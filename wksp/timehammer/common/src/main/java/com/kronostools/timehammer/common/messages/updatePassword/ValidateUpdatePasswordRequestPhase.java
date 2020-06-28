package com.kronostools.timehammer.common.messages.updatePassword;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.ValidatePhase;
import com.kronostools.timehammer.common.messages.ValidationError;
import com.kronostools.timehammer.common.messages.constants.ValidateResult;

import java.util.List;

@JsonDeserialize(builder = ValidateUpdatePasswordRequestPhaseBuilder.class)
public class ValidateUpdatePasswordRequestPhase extends ValidatePhase {
    ValidateUpdatePasswordRequestPhase(final ValidateResult result, final List<ValidationError> validationErrors) {
        super(result, validationErrors);
    }
}