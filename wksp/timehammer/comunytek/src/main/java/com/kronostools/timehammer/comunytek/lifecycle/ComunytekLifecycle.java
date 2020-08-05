package com.kronostools.timehammer.comunytek.lifecycle;

import com.kronostools.timehammer.comunytek.service.CredentialsCacheService;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class ComunytekLifecycle {
    private static final Logger LOG = LoggerFactory.getLogger(ComunytekLifecycle.class);

    final CredentialsCacheService credentialsCacheService;

    public ComunytekLifecycle(final CredentialsCacheService credentialsCacheService) {
        this.credentialsCacheService = credentialsCacheService;
    }

    void onStartup(@Observes StartupEvent event) {
        LOG.info("BEGIN onStartup");

        credentialsCacheService.loadCredentials();

        LOG.info("END onStartup");
    }

    void onShutdown(@Observes ShutdownEvent event) {
        LOG.info("BEGIN onShutdown");

        credentialsCacheService.dumpCredentials();

        LOG.info("END onShutdown");
    }
}