package com.kronostools.timehammer.core.model;

import com.kronostools.timehammer.common.messages.schedules.model.WorkerCurrentPreferences;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class WorkerCurrentPreferencesMultipleResultBuilder extends MultipleResultBuilder<WorkerCurrentPreferencesMultipleResultBuilder, WorkerCurrentPreferences> {

    public static WorkerCurrentPreferencesMultipleResult buildFromResult(final List<WorkerCurrentPreferences> workerCurrentPreferencesList) {
        return Optional.ofNullable(workerCurrentPreferencesList)
                .map(wcprb -> new WorkerCurrentPreferencesMultipleResultBuilder()
                        .result(wcprb)
                        .build())
                .orElse(new WorkerCurrentPreferencesMultipleResultBuilder()
                        .result(Collections.emptyList())
                        .build());
    }

    public static WorkerCurrentPreferencesMultipleResult buildFromError(final String errorMessage) {
        return new WorkerCurrentPreferencesMultipleResultBuilder()
                .errorMessage(errorMessage)
                .build();
    }

    public WorkerCurrentPreferencesMultipleResult build() {
        return new WorkerCurrentPreferencesMultipleResult(errorMessage, result);
    }
}
