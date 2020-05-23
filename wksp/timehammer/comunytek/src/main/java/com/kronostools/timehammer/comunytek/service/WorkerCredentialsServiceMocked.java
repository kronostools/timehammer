package com.kronostools.timehammer.comunytek.service;

import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.comunytek.constants.CredentialsResponse;
import com.kronostools.timehammer.comunytek.model.CachedWorkerCredentials;
import com.kronostools.timehammer.comunytek.model.CachedWorkerCredentialsBuilder;
import com.kronostools.timehammer.comunytek.model.WorkerCredentialsResponse;
import com.kronostools.timehammer.comunytek.model.WorkerCredentialsResponseBuilder;
import io.smallrye.mutiny.Uni;

public class WorkerCredentialsServiceMocked extends AbstractWorkerCredentialsService {

    public WorkerCredentialsServiceMocked(final TimeMachineService timeMachineService) {
        super(timeMachineService);
    }

    @Override
    public Uni<WorkerCredentialsResponse> getWorkerCredentials(final String internalId) {
        final CachedWorkerCredentials foundWorkerCredentials = cache.get(internalId, ii -> new CachedWorkerCredentialsBuilder()
                .externalPassword("blablabla")
                .expired(false)
                .build());

        final WorkerCredentialsResponse workerCredentialsResponse = new WorkerCredentialsResponseBuilder()
                .response(CredentialsResponse.OK)
                .externalPassword(foundWorkerCredentials.getExternalPassword())
                .build();

        return Uni.createFrom().item(workerCredentialsResponse);
    }
}