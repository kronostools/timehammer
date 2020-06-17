package com.kronostools.timehammer.comunytek.client;

import com.kronostools.timehammer.comunytek.constants.ComunytekLoginResult;
import com.kronostools.timehammer.comunytek.constants.ComunytekSimpleResult;
import com.kronostools.timehammer.comunytek.model.*;
import io.smallrye.mutiny.Uni;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ComunytekReactiveMockedClient implements ComunytekClient {

    private final Set<LocalDate> mockedHolidays;
    
    public ComunytekReactiveMockedClient() {
        final int currentYear = LocalDate.now().getYear();
        
        this.mockedHolidays = new HashSet<>() {{
            add(LocalDate.of(currentYear, 1, 1));
            add(LocalDate.of(currentYear, 2, 1));
            add(LocalDate.of(currentYear, 3, 1));
            add(LocalDate.of(currentYear, 4, 1));
            add(LocalDate.of(currentYear, 5, 1));
            add(LocalDate.of(currentYear, 6, 1));
            add(LocalDate.of(currentYear, 7, 1));
            add(LocalDate.of(currentYear, 8, 1));
            add(LocalDate.of(currentYear, 9, 1));
            add(LocalDate.of(currentYear, 10, 1));
            add(LocalDate.of(currentYear, 11, 1));
            add(LocalDate.of(currentYear, 12, 1));
            add(LocalDate.of(currentYear, 1, 5));
            add(LocalDate.of(currentYear, 2, 5));
            add(LocalDate.of(currentYear, 3, 5));
            add(LocalDate.of(currentYear, 4, 5));
            add(LocalDate.of(currentYear, 5, 5));
            add(LocalDate.of(currentYear, 6, 5));
            add(LocalDate.of(currentYear, 7, 5));
            add(LocalDate.of(currentYear, 8, 5));
            add(LocalDate.of(currentYear, 9, 5));
            add(LocalDate.of(currentYear, 10, 5));
            add(LocalDate.of(currentYear, 11, 5));
            add(LocalDate.of(currentYear, 12, 5));
            add(LocalDate.of(currentYear, 1, 10));
        }};
    }

    @Override
    public Uni<ComunytekLoginResponse> login(final String username, final String password) {
        if ("demo".equals(password)) {
            return Uni.createFrom().item(new ComunytekLoginResponseBuilder()
                    .result(ComunytekLoginResult.OK)
                    .fullname(username)
                    .username(username)
                    .sessionId(ComunytekLoginForm.FAKE_SESSIONID)
                    .build());
        } else {
            return Uni.createFrom().item(new ComunytekLoginResponseBuilder()
                    .result(ComunytekLoginResult.INVALID)
                    .errorMessage("Incorrect password. In demo mode, the password must be: demo")
                    .build());
        }
    }

    @Override
    public Uni<ComunytekHolidayResponse> isHoliday(final String username, final LocalDate holidayCandidate) {
        return Uni.createFrom().item(new ComunytekHolidayResponseBuilder()
                .result(ComunytekSimpleResult.OK)
                .holiday(mockedHolidays.contains(holidayCandidate))
                .build());
    }

    @Override
    public Uni<ComunytekStatusResponse> getStatus(final String username, final LocalDateTime timestamp) {
        return null;
    }

    @Override
    public boolean isMocked() {
        return true;
    }

    @Override
    public void dumpCredentials() {}

    @Override
    public void loadCredentials() {}
}