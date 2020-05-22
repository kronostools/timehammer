package com.kronostools.timehammer.core.model;

import java.util.List;

public class WorkerCurrentPreferencesMultipleResult extends MultipleResult<WorkerCurrentPreferences> {

    public WorkerCurrentPreferencesMultipleResult(final String errorMessage, final List<WorkerCurrentPreferences> workerCurrentPreferencesList) {
        super(errorMessage);
        this.result = workerCurrentPreferencesList;
    }
}