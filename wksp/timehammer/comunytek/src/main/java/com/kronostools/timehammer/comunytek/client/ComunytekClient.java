package com.kronostools.timehammer.comunytek.client;

import com.kronostools.timehammer.comunytek.constants.ComunytekAction;
import com.kronostools.timehammer.comunytek.model.ComunytekHolidayResponse;
import com.kronostools.timehammer.comunytek.model.ComunytekLoginResponse;
import com.kronostools.timehammer.comunytek.model.ComunytekStatusResponse;
import com.kronostools.timehammer.comunytek.model.ComunytekUpdateStatusResponse;
import io.smallrye.mutiny.Uni;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ComunytekClient {
    Uni<Boolean> about();

    Uni<ComunytekLoginResponse> login(final String username, final String password);

    Uni<ComunytekHolidayResponse> isHoliday(final String username, final LocalDate date);

    Uni<ComunytekStatusResponse> getStatus(final String username, final LocalDateTime timestamp);

    Uni<ComunytekUpdateStatusResponse> updateStatus(final String username, final ComunytekAction action, final LocalDateTime timestamp);
}