package com.kronostools.timehammer.auth;

import com.kronostools.timehammer.auth.service.WorkerCredentialsService;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.CredentialPhase;
import com.kronostools.timehammer.common.messages.schedules.CredentialPhaseBuilder;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorker;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorkerBuilder;
import com.kronostools.timehammer.common.utils.CommonUtils;
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
    public UpdateWorkersHolidayWorker process(final UpdateWorkersHolidayWorker inputWorker) {
        final UpdateWorkersHolidayWorker worker = UpdateWorkersHolidayWorkerBuilder.copy(inputWorker).build();

        LOG.debug("Updating credentials of worker '{}' ...", worker.getWorkerInternalId());

        final String credentials = workerCredentialsService.getWorkerCredentials(worker.getWorkerInternalId());

        final CredentialPhase credentialPhase;

        if (credentials == null) {
            final String message = CommonUtils.stringFormat("Missing credentials of worker '{}'", worker.getWorkerInternalId());

            LOG.warn(message);

            credentialPhase = new CredentialPhaseBuilder()
                    .errorMessage(message)
                    .build();
        } else {
            credentialPhase = new CredentialPhaseBuilder()
                    .externalPassword(credentials)
                    .build();
        }

        LOG.info("Updated credentials of worker");

        worker.setCredentialPhase(credentialPhase);

        return worker;
    }
}