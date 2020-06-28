package com.kronostools.timehammer.common.messages.updatePassword;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.WorkerCredentialsResult;

@JsonDeserialize(builder = CheckUpdatePasswordCredentialsPhaseBuilder.class)
public class CheckUpdatePasswordCredentialsPhase extends Phase<WorkerCredentialsResult> {
    CheckUpdatePasswordCredentialsPhase(final WorkerCredentialsResult result, final String errorMessage) {
        super(result, errorMessage);
    }
}