package com.kronostools.timehammer.comunytek.client;

import com.kronostools.timehammer.comunytek.model.ComunytekHolidayResponse;
import com.kronostools.timehammer.comunytek.model.ComunytekLoginResponse;
import io.smallrye.mutiny.Uni;

import java.time.LocalDate;

public interface ComunytekClient {
    Uni<ComunytekLoginResponse> login(String username, String password);

    Uni<ComunytekHolidayResponse> isHoliday(final String username, final LocalDate date);

    boolean isMocked();

    void dumpCredentials();

    void loadCredentials();
}