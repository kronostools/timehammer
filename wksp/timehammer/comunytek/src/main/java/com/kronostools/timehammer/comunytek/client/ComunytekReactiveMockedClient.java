package com.kronostools.timehammer.comunytek.client;

import com.kronostools.timehammer.common.utils.CommonUtils;
import com.kronostools.timehammer.comunytek.constants.ComunytekLoginResult;
import com.kronostools.timehammer.comunytek.constants.ComunytekSimpleResult;
import com.kronostools.timehammer.comunytek.constants.ComunytekStatusResult;
import com.kronostools.timehammer.comunytek.constants.ComunytekStatusValue;
import com.kronostools.timehammer.comunytek.model.*;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ComunytekReactiveMockedClient implements ComunytekClient {
    private static final Logger LOG = LoggerFactory.getLogger(ComunytekReactiveMockedClient.class);

    private final Map<String, String> mockedCredentials;
    private final Map<String, Map<LocalDate, List<ComunytekStatusResponse>>> mockedRegistry;
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

        this.mockedCredentials = new HashMap<>();
        this.mockedRegistry = new HashMap<>();
    }

    @Override
    public Uni<ComunytekLoginResponse> login(final String username, final String password) {
        if ("demo".equals(password)) {
            mockedCredentials.put(username, password);

            return Uni.createFrom().item(new ComunytekLoginResponseBuilder()
                    .result(ComunytekLoginResult.OK)
                    .fullname(username)
                    .username(username)
                    .sessionId(ComunytekLoginForm.FAKE_SESSIONID)
                    .build());
        } else {
            final String errorMessage = CommonUtils.stringFormat("Incorrect password for user '{}'. In demo mode, the password must be: demo", username);
            LOG.error(errorMessage);

            return Uni.createFrom().item(new ComunytekLoginResponseBuilder()
                    .result(ComunytekLoginResult.INVALID)
                    .errorMessage(errorMessage)
                    .build());
        }
    }

    @Override
    public Uni<ComunytekHolidayResponse> isHoliday(final String username, final LocalDate holidayCandidate) {
        if (mockedCredentials.containsKey(username)) {
            return Uni.createFrom().item(new ComunytekHolidayResponseBuilder()
                    .result(ComunytekSimpleResult.OK)
                    .holiday(mockedHolidays.contains(holidayCandidate))
                    .build());
        } else {
            final String errorMessage = CommonUtils.stringFormat("Credentials of user '{}' are missing or invalid.", username);
            LOG.error(errorMessage);

            return Uni.createFrom().item(new ComunytekHolidayResponseBuilder()
                    .result(ComunytekSimpleResult.KO)
                    .errorMessage(errorMessage)
                    .build());
        }
    }

    @Override
    public Uni<ComunytekStatusResponse> getStatus(final String username, final LocalDateTime timestamp) {
        final ComunytekStatusResponse result;

        if (mockedCredentials.containsKey(username)) {
            if (mockedRegistry.containsKey(username) && mockedRegistry.get(username).containsKey(timestamp.toLocalDate())) {
                final List<ComunytekStatusResponse> workerDayRegistry = mockedRegistry.get(username).get(timestamp.toLocalDate());

                result = workerDayRegistry.stream()
                        .skip(workerDayRegistry.size() - 1)
                        .findFirst()
                        .orElse(new ComunytekStatusResponseBuilder()
                                .result(ComunytekStatusResult.OK)
                                .date(timestamp.toLocalDate())
                                .status(ComunytekStatusValue.INITIAL)
                                .build());
            } else {
                result = new ComunytekStatusResponseBuilder()
                        .result(ComunytekStatusResult.OK)
                        .date(timestamp.toLocalDate())
                        .status(ComunytekStatusValue.INITIAL)
                        .build();
            }
        } else {
            final String errorMessage = CommonUtils.stringFormat("Credentials of user '{}' are missing or invalid.", username);
            LOG.error(errorMessage);

            result = new ComunytekStatusResponseBuilder()
                    .result(ComunytekStatusResult.MISSING_OR_INVALID_CREDENTIALS)
                    .errorMessage(errorMessage)
                    .build();
        }

        return Uni.createFrom().item(result);
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