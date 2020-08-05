package com.kronostools.timehammer.comunytek.config;

import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.client.impl.ComunytekReactiveMockedClient;
import com.kronostools.timehammer.comunytek.client.impl.ComunytekReactiveRealClient;
import com.kronostools.timehammer.comunytek.client.impl.DefaultComunytekClient;
import com.kronostools.timehammer.comunytek.service.CredentialsCacheService;
import io.quarkus.arc.DefaultBean;
import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class ComunytekClientConfig {
    @ConfigProperty(name = "timehammer.mocks.comunytekclient", defaultValue = "false")
    Boolean comunytekClientMocked;

    @Produces
    @ApplicationScoped
    @DefaultBean
    public ComunytekClient comunytekClient(final Vertx vertx, final LoginCacheConfig loginCacheConfig, final TimeMachineService timeMachineService, final CredentialsCacheService credentialsCacheService) {
        final ComunytekReactiveRealClient comunytekReactiveRealClient = new ComunytekReactiveRealClient(vertx, loginCacheConfig, timeMachineService, credentialsCacheService);
        final ComunytekReactiveMockedClient comunytekReactiveMockedClient = new ComunytekReactiveMockedClient(credentialsCacheService);

        return new DefaultComunytekClient(comunytekReactiveMockedClient, comunytekReactiveRealClient, comunytekClientMocked);
    }
}