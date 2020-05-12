package com.kronostools.timehammer.comunytek.client;

import com.kronostools.timehammer.comunytek.model.ComunytekHolidayResponse;
import io.smallrye.mutiny.Uni;

import java.time.LocalDate;

public interface ComunytekClient {
    Uni<ComunytekHolidayResponse> isHoliday(final String username, final String password, final LocalDate date);

    boolean isMocked();
}