package com.kronostools.timehammer.comunytek.service;

import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.constants.ComunytekLoginResult;
import com.kronostools.timehammer.comunytek.model.ComunytekLoginResponse;
import com.kronostools.timehammer.comunytek.model.ComunytekLoginResponseBuilder;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerCredentialValidatorService {
    private final ComunytekClient comunytekClient;

    public WorkerCredentialValidatorService(final ComunytekClient comunytekClient) {
        this.comunytekClient = comunytekClient;
    }

    public Uni<ComunytekLoginResponse> checkWorkerCredentials(final String workerExternalId, final String workerExternalPassword) {
        return comunytekClient.login(workerExternalId, workerExternalPassword)
                .onFailure(Exception.class)
                .recoverWithItem((e) -> new ComunytekLoginResponseBuilder()
                        .result(ComunytekLoginResult.KO)
                        .errorMessage(e.getMessage())
                        .build());
    }
}