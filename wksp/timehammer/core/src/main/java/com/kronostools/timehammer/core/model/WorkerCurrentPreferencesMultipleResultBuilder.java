package com.kronostools.timehammer.core.model;

import java.util.List;
import java.util.Optional;

public class WorkerCurrentPreferencesMultipleResultBuilder extends MultipleResultBuilder<WorkerCurrentPreferencesMultipleResultBuilder, WorkerCurrentPreferences> {

    public static WorkerCurrentPreferencesMultipleResult buildFromResult(final List<WorkerCurrentPreferences> workerCurrentPreferencesList) {
        return Optional.ofNullable(workerCurrentPreferencesList)
                .map(wcprb -> new WorkerCurrentPreferencesMultipleResultBuilder()
                        .result(wcprb)
                        .build())
                .orElse(null);
    }

    public static WorkerCurrentPreferencesMultipleResult buildFromError(final String errorMessage) {
        return Optional.ofNullable(errorMessage)
                .map(em -> new WorkerCurrentPreferencesMultipleResultBuilder()
                        .errorMessage(em)
                        .build())
                .orElse(null);
    }

    public WorkerCurrentPreferencesMultipleResult build() {
        return new WorkerCurrentPreferencesMultipleResult(errorMessage, result);
    }
}
