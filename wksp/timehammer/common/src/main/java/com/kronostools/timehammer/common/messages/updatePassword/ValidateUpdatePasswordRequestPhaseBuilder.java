package com.kronostools.timehammer.common.messages.updatePassword;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.ValidatePhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.ValidateResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class ValidateUpdatePasswordRequestPhaseBuilder extends ValidatePhaseBuilder<ValidateUpdatePasswordRequestPhaseBuilder, ValidateUpdatePasswordRequestPhase> {

    public static ValidateUpdatePasswordRequestPhase copyAndBuild(final ValidateUpdatePasswordRequestPhase validateUpdatePasswordRequestPhase) {
        return Optional.ofNullable(validateUpdatePasswordRequestPhase)
                .map(vuprp -> ValidateUpdatePasswordRequestPhaseBuilder.copy(vuprp).build())
                .orElse(null);
    }

    public static ValidateUpdatePasswordRequestPhaseBuilder copy(final ValidateUpdatePasswordRequestPhase validateUpdatePasswordRequestPhase) {
        return Optional.ofNullable(validateUpdatePasswordRequestPhase)
                .map(vuprp -> new ValidateUpdatePasswordRequestPhaseBuilder()
                        .validationErrors(vuprp.getValidationErrors()))
                .orElse(null);
    }

    @Override
    public ValidateUpdatePasswordRequestPhase build() {
        return new ValidateUpdatePasswordRequestPhase(validationErrors.isEmpty() ? ValidateResult.VALID : ValidateResult.INVALID, validationErrors);
    }
}