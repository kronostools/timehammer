package com.kronostools.timehammer.core.model;

import java.util.Optional;

public class WorkerCurrentPreferencesSingleResultBuilder extends SingleResultBuilder<WorkerCurrentPreferencesSingleResultBuilder, WorkerCurrentPreferences> {

    public static WorkerCurrentPreferencesSingleResult buildFromResult(final WorkerCurrentPreferencesBuilder workerCurrentPreferencesBuilder) {
        return Optional.ofNullable(workerCurrentPreferencesBuilder)
                .map(wcprb -> new WorkerCurrentPreferencesSingleResultBuilder()
                        .result(wcprb.build())
                        .build())
                .orElse(new WorkerCurrentPreferencesSingleResultBuilder().build());
    }

    public static WorkerCurrentPreferencesSingleResult buildFromError(final String errorMessage) {
        return new WorkerCurrentPreferencesSingleResultBuilder()
                .errorMessage(errorMessage)
                .build();
    }

    public WorkerCurrentPreferencesSingleResult build() {
        return new WorkerCurrentPreferencesSingleResult(errorMessage, result);
    }
}
