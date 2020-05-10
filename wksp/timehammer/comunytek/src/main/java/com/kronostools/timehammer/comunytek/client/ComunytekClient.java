package com.kronostools.timehammer.comunytek.client;

import com.kronostools.timehammer.comunytek.model.HolidayResponse;
import io.smallrye.mutiny.Uni;

import java.time.LocalDate;

public interface ComunytekClient {
    Uni<HolidayResponse> isHoliday(final String username, final String password, final LocalDate date);

    boolean isMocked();
}