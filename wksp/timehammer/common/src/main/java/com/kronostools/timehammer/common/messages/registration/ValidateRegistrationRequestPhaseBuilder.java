package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class ValidateRegistrationRequestPhaseBuilder extends PhaseBuilder<SimpleResult, ValidateRegistrationRequestPhaseBuilder> {

    public static ValidateRegistrationRequestPhase copyAndBuild(final ValidateRegistrationRequestPhase validateRegistrationRequestPhase) {
        return Optional.ofNullable(validateRegistrationRequestPhase)
                .map(vrrp -> ValidateRegistrationRequestPhaseBuilder.copy(vrrp).build())
                .orElse(null);
    }

    public static ValidateRegistrationRequestPhaseBuilder copy(final ValidateRegistrationRequestPhase validateRegistrationRequestPhase) {
        return Optional.ofNullable(validateRegistrationRequestPhase)
                .map(vrrp -> new ValidateRegistrationRequestPhaseBuilder()
                        .result(vrrp.getResult())
                        .errorMessage(vrrp.getErrorMessage()))
                .orElse(null);
    }

    public ValidateRegistrationRequestPhase build() {
        return new ValidateRegistrationRequestPhase(result, errorMessage);
    }
}