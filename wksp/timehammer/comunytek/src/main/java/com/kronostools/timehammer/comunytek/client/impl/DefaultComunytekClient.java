package com.kronostools.timehammer.comunytek.client.impl;

import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.constants.ComunytekAction;
import com.kronostools.timehammer.comunytek.model.ComunytekHolidayResponse;
import com.kronostools.timehammer.comunytek.model.ComunytekLoginResponse;
import com.kronostools.timehammer.comunytek.model.ComunytekStatusResponse;
import com.kronostools.timehammer.comunytek.model.ComunytekUpdateStatusResponse;
import io.smallrye.mutiny.Uni;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DefaultComunytekClient extends AbstractComunytekClient {
    private final ComunytekReactiveMockedClient comunytekReactiveMockedClient;
    private final ComunytekReactiveRealClient comunytekReactiveRealClient;
    private final boolean comunytekClientMocked;

    public DefaultComunytekClient(final ComunytekReactiveMockedClient comunytekReactiveMockedClient, final ComunytekReactiveRealClient comunytekReactiveRealClient, final boolean comunytekClientMocked) {
        this.comunytekReactiveMockedClient = comunytekReactiveMockedClient;
        this.comunytekReactiveRealClient = comunytekReactiveRealClient;
        this.comunytekClientMocked = comunytekClientMocked;
    }

    @Override
    public Uni<Boolean> about() {
        return getService().about();
    }

    @Override
    public Uni<ComunytekLoginResponse> login(String username, String password) {
        return getService().login(username, password);
    }

    @Override
    public Uni<ComunytekHolidayResponse> isHoliday(String username, LocalDate date) {
        return getService().isHoliday(username, date);
    }

    @Override
    public Uni<ComunytekStatusResponse> getStatus(String username, LocalDateTime timestamp) {
        return getService().getStatus(username, timestamp);
    }

    @Override
    public Uni<ComunytekUpdateStatusResponse> updateStatus(String username, ComunytekAction action, LocalDateTime timestamp) {
        return getService().updateStatus(username, action, timestamp);
    }

    private ComunytekClient getService() {
        if (comunytekClientMocked) {
            return comunytekReactiveMockedClient;
        } else {
            return comunytekReactiveRealClient;
        }
    }
}
