package com.kronostools.timehammer.statemachine.lifecycle;

import com.kronostools.timehammer.statemachine.service.WorkerWaitService;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class StateMachineLifecycle {
    private static final Logger LOG = LoggerFactory.getLogger(StateMachineLifecycle.class);

    final WorkerWaitService workerWaitService;

    public StateMachineLifecycle(final WorkerWaitService workerWaitService) {
        this.workerWaitService = workerWaitService;
    }

    void onStartup(@Observes StartupEvent event) {
        LOG.info("BEGIN onStartup");

        workerWaitService.loadWaits();

        LOG.info("END onStartup");
    }

    void onShutdown(@Observes ShutdownEvent event) {
        LOG.info("BEGIN onShutdown");

        workerWaitService.dumpWaits();

        LOG.info("END onShutdown");
    }
}