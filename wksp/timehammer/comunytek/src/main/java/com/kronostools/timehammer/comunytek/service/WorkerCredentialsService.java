package com.kronostools.timehammer.comunytek.service;

import com.kronostools.timehammer.comunytek.model.WorkerCredentialsResponse;
import io.smallrye.mutiny.Uni;

import java.util.concurrent.CompletionStage;

public interface WorkerCredentialsService {
    CompletionStage<Void> updateWorkerCredentials(String internalId, String externalPassword);

    CompletionStage<Void> markAsExpired(String internalId);

    Uni<WorkerCredentialsResponse> getWorkerCredentials(String internalId);

    void dump();

    void load();
}
