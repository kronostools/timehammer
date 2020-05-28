package com.kronostools.timehammer.comunytek.lifecycle;

import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class AuthLifecycle {
    private static final Logger LOG = LoggerFactory.getLogger(AuthLifecycle.class);

    final ComunytekClient comunytekClient;

    public AuthLifecycle(final ComunytekClient comunytekClient) {
        this.comunytekClient = comunytekClient;
    }

    void onStartup(@Observes StartupEvent event) {
        LOG.info("BEGIN onStartup");

        LOG.info("Loading worker credentials from temp dump file ...");

        comunytekClient.loadCredentials();

        LOG.info("Loaded worker credentials");

        LOG.info("END onStartup");
    }

    void onShutdown(@Observes ShutdownEvent event) {
        LOG.info("BEGIN onShutdown");

        LOG.debug("Dumping worker credentials to temp dump file ...");

        comunytekClient.dumpCredentials();

        LOG.debug("Dumped worker credentials");

        LOG.info("END onShutdown");
    }
}