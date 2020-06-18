package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.WorkerStatusAction;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

@JsonDeserialize(builder = WorkerStatusActionPhaseBuilder.class)
public class WorkerStatusActionPhase extends Phase<SimpleResult> {
    private WorkerStatusAction workerStatusAction;

    WorkerStatusActionPhase(final SimpleResult result, final String errorMessage) {
        super(result, errorMessage);
    }

    public WorkerStatusAction getWorkerStatusAction() {
        return workerStatusAction;
    }

    public void setWorkerStatusAction(WorkerStatusAction workerStatusAction) {
        this.workerStatusAction = workerStatusAction;
    }
}