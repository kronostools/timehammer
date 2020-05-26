package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class CheckRegistrationRequestPhaseBuilder extends PhaseBuilder<SimpleResult, CheckRegistrationRequestPhaseBuilder> {
    private String chatId;

    public static CheckRegistrationRequestPhase copyAndBuild(final CheckRegistrationRequestPhase checkRegistrationRequestPhase) {
        return Optional.ofNullable(checkRegistrationRequestPhase)
                .map(chp -> CheckRegistrationRequestPhaseBuilder.copy(chp).build())
                .orElse(null);
    }

    public static CheckRegistrationRequestPhaseBuilder copy(final CheckRegistrationRequestPhase checkRegistrationRequestPhase) {
        return Optional.ofNullable(checkRegistrationRequestPhase)
                .map(chp -> new CheckRegistrationRequestPhaseBuilder()
                        .result(chp.getResult())
                        .errorMessage(chp.getErrorMessage())
                        .chatId(chp.getChatId()))
                .orElse(null);
    }

    public CheckRegistrationRequestPhaseBuilder chatId(final String chatId) {
        this.chatId = chatId;
        return this;
    }

    public CheckRegistrationRequestPhase build() {
        final CheckRegistrationRequestPhase chp = new CheckRegistrationRequestPhase(result, errorMessage);
        chp.setChatId(chatId);

        return chp;
    }
}