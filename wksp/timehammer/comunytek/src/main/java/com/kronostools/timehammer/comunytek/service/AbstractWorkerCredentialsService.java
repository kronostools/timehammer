package com.kronostools.timehammer.comunytek.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.comunytek.model.CachedWorkerCredentials;
import com.kronostools.timehammer.comunytek.model.CachedWorkerCredentialsBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public abstract class AbstractWorkerCredentialsService implements WorkerCredentialsService {
    final Cache<String, CachedWorkerCredentials> cache;

    final TimeMachineService timeMachineService;

    AbstractWorkerCredentialsService(final TimeMachineService timeMachineService) {
        cache = Caffeine.newBuilder()
                .build();

        this.timeMachineService = timeMachineService;
    }

    @Override
    public CompletionStage<Void> updateWorkerCredentials(final String internalId, final String externalPassword) {
        cache.put(internalId, new CachedWorkerCredentialsBuilder()
                .externalPassword(externalPassword)
                .expired(false)
                .build());

        return CompletableFuture.completedStage(null);
    }

    @Override
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

    @Override
    public void dump() {
        // TODO: implement logic
    }

    @Override
    public void load() {
        // TODO; implement logic
    }
}