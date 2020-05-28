package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.WorkerCredentialsResult;

@JsonDeserialize(builder = CheckRegistrationRequestPhaseBuilder.class)
public class CheckWorkerCredentialsPhase extends Phase<WorkerCredentialsResult> {
    CheckWorkerCredentialsPhase(final WorkerCredentialsResult result, final String errorMessage) {
        super(result, errorMessage);
    }
}