package com.kronostools.timehammer.core.model;

public class WorkerCurrentPreferencesSingleResult extends SingleResult<WorkerCurrentPreferences> {

    public WorkerCurrentPreferencesSingleResult(final String errorMessage, final WorkerCurrentPreferences workerCurrentPreferences) {
        super(errorMessage);
        this.result = workerCurrentPreferences;
    }
}