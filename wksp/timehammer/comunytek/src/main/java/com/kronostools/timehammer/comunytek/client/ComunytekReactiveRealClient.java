package com.kronostools.timehammer.comunytek.client;

import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

public class ComunytekReactiveRealClient implements ComunytekClient {
    private final WebClient client;

    public ComunytekReactiveRealClient(final Vertx vertx) {
        this.client = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost("empleados.comunytek.com/SWHandler")
                .setDefaultPort(443)
                .setSsl(true)
                .setTrustAll(true));
    }

    @Override
    public boolean isMocked() {
        return false;
    }
}