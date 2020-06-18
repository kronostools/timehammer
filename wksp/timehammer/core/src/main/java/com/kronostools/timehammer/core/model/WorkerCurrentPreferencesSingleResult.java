package com.kronostools.timehammer.core.model;

import com.kronostools.timehammer.common.messages.schedules.model.WorkerCurrentPreferences;

public class WorkerCurrentPreferencesSingleResult extends SingleResult<WorkerCurrentPreferences> {

    public WorkerCurrentPreferencesSingleResult(final String errorMessage, final WorkerCurrentPreferences workerCurrentPreferences) {
        super(errorMessage);
        this.result = workerCurrentPreferences;
    }
}