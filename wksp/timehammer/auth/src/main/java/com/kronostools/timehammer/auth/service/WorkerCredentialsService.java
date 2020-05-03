package com.kronostools.timehammer.auth.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerCredentialsService {
    private final Cache<String, String> cache;

    public WorkerCredentialsService() {
        cache = Caffeine.newBuilder()
                .build();
    }

    public void updateWorkerCredentials(final String internalId, final String externalPassword) {
        cache.put(internalId, externalPassword);
    }

    public String getWorkerCredentials(final String internalId) {
        return cache.getIfPresent(internalId);
    }

    public void dump() {
        // TODO: implement logic
    }

    public void load() {
        // TODO; implement logic
    }
}