package com.kronostools.timehammer.auth;

import com.kronostools.timehammer.auth.service.WorkerCredentialsService;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidaysWorker;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UpdateWorkersHolidaysWorkerAuthenticator {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateWorkersHolidaysWorkerAuthenticator.class);

    private final WorkerCredentialsService workerCredentialsService;

    public UpdateWorkersHolidaysWorkerAuthenticator(final WorkerCredentialsService workerCredentialsService) {
        this.workerCredentialsService = workerCredentialsService;
    }

    @Incoming(Channels.HOLIDAYS_WORKER_AUTH)
    @Outgoing(Channels.HOLIDAYS_WORKER_GET)
    public UpdateWorkersHolidaysWorker process(final UpdateWorkersHolidaysWorker worker) {
        LOG.debug("Updating credentials of worker '{}' ...", worker.getWorkerInternalId());

        final String credentials = workerCredentialsService.getWorkerCredentials(worker.getWorkerInternalId());

        if (credentials == null) {
            LOG.warn("Missing credentials of worker '{}'", worker.getWorkerInternalId());

            worker.setUpdatedSuccessfully(false);
        } else {
            worker.setExternalPassword(credentials);
        }

        LOG.info("Updated credentials of worker");

        return worker;
    }
}