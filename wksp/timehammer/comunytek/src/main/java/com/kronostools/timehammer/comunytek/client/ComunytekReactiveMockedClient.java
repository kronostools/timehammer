package com.kronostools.timehammer.comunytek.client;

import com.kronostools.timehammer.comunytek.model.HolidayResponse;
import io.smallrye.mutiny.Uni;

import java.time.LocalDate;
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
    public Uni<HolidayResponse> isHoliday(final String username, final String password, final LocalDate date) {
        return Uni.createFrom().item(() -> {
            final HolidayResponse result = new HolidayResponse();
            result.setSuccessful(true);
            result.setHoliday(mockedHolidays.contains(date));

            return result;
        });
    }

    @Override
    public boolean isMocked() {
        return true;
    }
}