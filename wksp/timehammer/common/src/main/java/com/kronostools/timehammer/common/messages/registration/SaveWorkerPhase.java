package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.SaveWorkerResult;

@JsonDeserialize(builder = CheckRegistrationRequestPhaseBuilder.class)
public class SaveWorkerPhase extends Phase<SaveWorkerResult> {
    SaveWorkerPhase(final SaveWorkerResult result, final String errorMessage) {
        super(result, errorMessage);
    }
}