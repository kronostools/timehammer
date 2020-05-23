package com.kronostools.timehammer.comunytek.service;

import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.comunytek.constants.CredentialsResponse;
import com.kronostools.timehammer.comunytek.model.CachedWorkerCredentials;
import com.kronostools.timehammer.comunytek.model.WorkerCredentialsResponse;
import com.kronostools.timehammer.comunytek.model.WorkerCredentialsResponseBuilder;
import io.smallrye.mutiny.Uni;

public class WorkerCredentialsServiceReal extends AbstractWorkerCredentialsService {

    public WorkerCredentialsServiceReal(final TimeMachineService timeMachineService) {
        super(timeMachineService);
    }

    @Override
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
}