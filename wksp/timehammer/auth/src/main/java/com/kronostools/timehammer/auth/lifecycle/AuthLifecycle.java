package com.kronostools.timehammer.auth.lifecycle;

import com.kronostools.timehammer.auth.service.WorkerCredentialsService;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class AuthLifecycle {
    private static final Logger LOG = LoggerFactory.getLogger(AuthLifecycle.class);

    final WorkerCredentialsService workerCredentialsService;

    public AuthLifecycle(final WorkerCredentialsService workerCredentialsService) {
        this.workerCredentialsService = workerCredentialsService;
    }

    void onStartup(@Observes StartupEvent event) {
        LOG.info("BEGIN onStartup");

        LOG.info("Loading worker credentials from temp dump file ...");

        workerCredentialsService.load();

        LOG.info("Loaded worker credentials");

        LOG.info("END onStartup");
    }

    void onShutdown(@Observes ShutdownEvent event) {
        LOG.info("BEGIN onShutdown");

        LOG.debug("Dumping worker credentials to temp dump file ...");

        workerCredentialsService.dump();

        LOG.debug("Dumped worker credentials");

        LOG.info("END onShutdown");
    }
}