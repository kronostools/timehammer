package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.ValidatePhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.SimpleValidateResult;
import com.kronostools.timehammer.common.messages.registration.forms.RegistrationRequestFormValidated;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class ValidateRegistrationRequestPhaseBuilder extends ValidatePhaseBuilder<SimpleValidateResult, RegistrationRequestFormValidated, ValidateRegistrationRequestPhaseBuilder, ValidateRegistrationRequestPhase> {

    public static ValidateRegistrationRequestPhase copyAndBuild(final ValidateRegistrationRequestPhase validateRegistrationRequestPhase) {
        return Optional.ofNullable(validateRegistrationRequestPhase)
                .map(vrrp -> ValidateRegistrationRequestPhaseBuilder.copy(vrrp).build())
                .orElse(null);
    }

    public static ValidateRegistrationRequestPhaseBuilder copy(final ValidateRegistrationRequestPhase validateRegistrationRequestPhase) {
        return Optional.ofNullable(validateRegistrationRequestPhase)
                .map(vrrp -> new ValidateRegistrationRequestPhaseBuilder()
                        .result(vrrp.getResult())
                        .validatedForm(vrrp.getValidatedForm())
                        .validationErrors(vrrp.getValidationErrors()))
                .orElse(null);
    }

    @Override
    public ValidateRegistrationRequestPhase build() {
        return new ValidateRegistrationRequestPhase(result, validatedForm, validationErrors);
    }
}