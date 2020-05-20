package com.kronostools.timehammer.comunytek.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.comunytek.constants.CredentialsResponse;
import com.kronostools.timehammer.comunytek.model.CachedWorkerCredentials;
import com.kronostools.timehammer.comunytek.model.CachedWorkerCredentialsBuilder;
import com.kronostools.timehammer.comunytek.model.WorkerCredentialsResponse;
import com.kronostools.timehammer.comunytek.model.WorkerCredentialsResponseBuilder;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class WorkerCredentialsService {
    private final Cache<String, CachedWorkerCredentials> cache;

    private final TimeMachineService timeMachineService;

    public WorkerCredentialsService(final TimeMachineService timeMachineService) {
        cache = Caffeine.newBuilder()
                .build();

        this.timeMachineService = timeMachineService;
    }

    public CompletionStage<Void> updateWorkerCredentials(final String internalId, final String externalPassword) {
        cache.put(internalId, new CachedWorkerCredentialsBuilder()
                .externalPassword(externalPassword)
                .expired(false)
                .build());

        return CompletableFuture.completedStage(null);
    }

    public CompletionStage<Void> markAsExpired(final String internalId) {
        final CachedWorkerCredentials foundWorkerCredentials = cache.getIfPresent(internalId);

        if (foundWorkerCredentials != null) {
            cache.put(internalId, CachedWorkerCredentialsBuilder.copy(foundWorkerCredentials)
                    .expired(true)
                    .expiredSince(timeMachineService.getNow())
                    .build());
        }

        return CompletableFuture.completedStage(null);
    }

    public Uni<WorkerCredentialsResponse> getWorkerCredentials(final String internalId) {
        final CachedWorkerCredentials foundWorkerCredentials = cache.getIfPresent(internalId);

        final WorkerCredentialsResponse workerCredentialsResponse;

        if (foundWorkerCredentials == null) {
            workerCredentialsResponse = new WorkerCredentialsResponseBuilder()
                    .response(CredentialsResponse.NOT_FOUND)
                    .build();
        } else if (foundWorkerCredentials.isExpired()) {
            workerCredentialsResponse = new WorkerCredentialsResponseBuilder()
                    .response(CredentialsResponse.EXPIRED)
                    .build();
        } else {
            workerCredentialsResponse = new WorkerCredentialsResponseBuilder()
                    .response(CredentialsResponse.OK)
                    .externalPassword(foundWorkerCredentials.getExternalPassword())
                    .build();
        }

        return Uni.createFrom().item(workerCredentialsResponse);
    }

    public void dump() {
        // TODO: implement logic
    }

    public void load() {
        // TODO; implement logic
    }
}