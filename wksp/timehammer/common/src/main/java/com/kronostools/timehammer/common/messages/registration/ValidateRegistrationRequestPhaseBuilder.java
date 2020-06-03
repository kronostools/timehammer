package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.ValidatePhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.ValidateResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class ValidateRegistrationRequestPhaseBuilder extends ValidatePhaseBuilder<ValidateRegistrationRequestPhaseBuilder, ValidateRegistrationRequestPhase> {

    public static ValidateRegistrationRequestPhase copyAndBuild(final ValidateRegistrationRequestPhase validateRegistrationRequestPhase) {
        return Optional.ofNullable(validateRegistrationRequestPhase)
                .map(vrrp -> ValidateRegistrationRequestPhaseBuilder.copy(vrrp).build())
                .orElse(null);
    }

    public static ValidateRegistrationRequestPhaseBuilder copy(final ValidateRegistrationRequestPhase validateRegistrationRequestPhase) {
        return Optional.ofNullable(validateRegistrationRequestPhase)
                .map(vrrp -> new ValidateRegistrationRequestPhaseBuilder()
                        .validationErrors(vrrp.getValidationErrors()))
                .orElse(null);
    }

    @Override
    public ValidateRegistrationRequestPhase build() {
        return new ValidateRegistrationRequestPhase(validationErrors.isEmpty() ? ValidateResult.VALID : ValidateResult.INVALID, validationErrors);
    }
}