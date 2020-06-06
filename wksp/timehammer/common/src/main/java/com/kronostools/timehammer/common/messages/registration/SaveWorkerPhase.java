package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

@JsonDeserialize(builder = SaveWorkerPhaseBuilder.class)
public class SaveWorkerPhase extends Phase<SimpleResult> {
    SaveWorkerPhase(final SimpleResult result, final String errorMessage) {
        super(result, errorMessage);
    }
}