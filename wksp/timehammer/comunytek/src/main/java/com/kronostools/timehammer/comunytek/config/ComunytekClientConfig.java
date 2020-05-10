package com.kronostools.timehammer.comunytek.config;

import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.client.ComunytekReactiveMockedClient;
import com.kronostools.timehammer.comunytek.client.ComunytekReactiveRealClient;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.vertx.mutiny.core.Vertx;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class ComunytekClientConfig {
    @Produces
    @ApplicationScoped
    @DefaultBean
    public ComunytekClient realComunytekClient(final Vertx vertx, final LoginCacheConfig loginCacheConfig) {
        return new ComunytekReactiveRealClient(vertx, loginCacheConfig);
    }

    @Produces
    @ApplicationScoped
    @UnlessBuildProfile("prod")
    public ComunytekClient mockedComunytekClient() {
        return new ComunytekReactiveMockedClient();
    }
}