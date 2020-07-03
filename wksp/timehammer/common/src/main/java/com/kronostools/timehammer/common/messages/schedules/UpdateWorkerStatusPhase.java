package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.WorkerStatusResult;

@JsonDeserialize(builder = UpdateWorkerStatusPhaseBuilder.class)
public class UpdateWorkerStatusPhase extends Phase<WorkerStatusResult> {
    UpdateWorkerStatusPhase(final WorkerStatusResult result, final String errorMessage) {
        super(result, errorMessage);
    }
}