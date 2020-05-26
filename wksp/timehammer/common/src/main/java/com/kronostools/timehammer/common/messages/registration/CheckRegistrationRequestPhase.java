package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

@JsonDeserialize(builder = CheckRegistrationRequestPhaseBuilder.class)
public class CheckRegistrationRequestPhase extends Phase<SimpleResult> {
    private String chatId;

    CheckRegistrationRequestPhase(final SimpleResult result, final String errorMessage) {
        super(result, errorMessage);
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}