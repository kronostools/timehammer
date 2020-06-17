package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.WorkerStatusContext;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

@JsonDeserialize(builder = GetWorkerStatusPhaseBuilder.class)
public class GetWorkerStatusPhase extends Phase<SimpleResult> {
    private WorkerStatusContext statusContext;

    GetWorkerStatusPhase(final SimpleResult result, final String errorMessage) {
        super(result, errorMessage);
    }

    public WorkerStatusContext getStatusContext() {
        return statusContext;
    }

    public void setStatusContext(WorkerStatusContext statusContext) {
        this.statusContext = statusContext;
    }
}