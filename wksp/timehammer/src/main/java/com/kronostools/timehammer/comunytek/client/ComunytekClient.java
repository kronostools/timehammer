package com.kronostools.timehammer.comunytek.client;

import com.kronostools.timehammer.comunytek.dto.ComunytekActionResponseDto;
import com.kronostools.timehammer.comunytek.dto.ComunytekHolidaysDto;
import com.kronostools.timehammer.comunytek.dto.ComunytekSessionDto;
import com.kronostools.timehammer.comunytek.dto.ComunytekStatusDto;
import com.kronostools.timehammer.comunytek.enums.ComunytekAction;
import com.kronostools.timehammer.comunytek.exceptions.ComunytekAuthenticationException;
import com.kronostools.timehammer.comunytek.restclient.ComunytekMockedClient;
import com.kronostools.timehammer.comunytek.restclient.ComunytekRealRestClient;
import com.kronostools.timehammer.comunytek.restclient.ComunytekRestClient;
import com.kronostools.timehammer.comunytek.utils.ComunytekConstants;
import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.enums.SupportedTimezone;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.utils.Constants;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class ComunytekClient {
    private static final Logger LOG = LoggerFactory.getLogger(ComunytekClient.class);

    private final TimehammerConfig timehammerConfig;
    private final ComunytekRealRestClient comunytekRealClient;
    private final ComunytekMockedClient comunytekMockedClient;

    public ComunytekClient(final TimehammerConfig timehammerConfig,
                           @RestClient final ComunytekRealRestClient comunytekRealClient,
                           final ComunytekMockedClient comunytekMockedClient) {
        this.timehammerConfig = timehammerConfig;
        this.comunytekRealClient = comunytekRealClient;
        this.comunytekMockedClient = comunytekMockedClient;
    }

    public String about() {
        return getClient().about();
    }

    @CacheResult(cacheName = Constants.Caches.COMUNYTEK_SESSION)
    public ComunytekSessionDto login(final String username, final String password) {
        LOG.debug("BEGIN login: [{}] [{}]", username, Constants.HIDDEN_PASSWORD);

        final String loginResponse = getClient().login(ComunytekConstants.FAKE_SESSIONID, ComunytekConstants.ACTION_LOGIN, username, password, ComunytekConstants.S);

        if (loginResponse.startsWith("ERROR")) {
            throw new ComunytekAuthenticationException();
        }

        LOG.debug("END login");

        return ComunytekSessionDto.fromResponse(username, loginResponse);
    }

    @CacheInvalidate(cacheName = Constants.Caches.COMUNYTEK_SESSION)
    public void invalidateSession(final ComunytekSessionDto comunytekSessionDto, @CacheKey final String username, @CacheKey final String password) {
        LOG.info("Comunytek session [{}] has expired, invalidating it ...", comunytekSessionDto);
    }

    public ComunytekHolidaysDto getHolidays(final String username, final String password) {
        LOG.debug("BEGIN getHolidays: [{}] [{}]", username, Constants.HIDDEN_PASSWORD);

        ComunytekSessionDto comunytekSessionDto = this.login(username, password);

        String holidaysResponse = getClient().getHolidays(comunytekSessionDto.getSessionId(), ComunytekConstants.ACTION_HOLIDAYS, comunytekSessionDto.getUsername(), comunytekSessionDto.getUsername(), ComunytekConstants.N);

        if (ComunytekConstants.ERROR_EXPIRED_SESSION.equals(holidaysResponse)) {
            this.invalidateSession(comunytekSessionDto, username, password);
            comunytekSessionDto = this.login(username, password);
            holidaysResponse = getClient().getHolidays(comunytekSessionDto.getSessionId(), ComunytekConstants.ACTION_HOLIDAYS, comunytekSessionDto.getUsername(), comunytekSessionDto.getUsername(), ComunytekConstants.N);
        }

        LOG.debug("END getHolidays");

        return ComunytekHolidaysDto.fromResponse(comunytekSessionDto.getUsername(), holidaysResponse);
    }

    @CacheResult(cacheName = Constants.Caches.COMUNYTEK_STATUS)
    public ComunytekStatusDto getStatus(@CacheKey final String username, @CacheKey final String password, final LocalDateTime timestamp) {
        LOG.debug("BEGIN getStatus: [{}] [{}] [{}]", username, Constants.HIDDEN_PASSWORD, timestamp);

        ComunytekSessionDto comunytekSessionDto = this.login(username, password);

        final String dayFormatted;

        if (isClientMocked()) {
            dayFormatted = TimeMachineService.formatDateTime(timestamp, TimeMachineService.FORMAT_YYYYMMDDTHHMM);
        } else {
            dayFormatted = TimeMachineService.formatDateTime(timestamp, TimeMachineService.FORMAT_YYYYMMDD);
        }

        String hoursReportedResponse = getClient().getDayRegistry(comunytekSessionDto.getSessionId(), ComunytekConstants.ACTION_HOURS_REPORTED, comunytekSessionDto.getUsername(), comunytekSessionDto.getUsername(), dayFormatted);

        if (ComunytekConstants.ERROR_EXPIRED_SESSION.equals(hoursReportedResponse)) {
            this.invalidateSession(comunytekSessionDto, username, password);
            comunytekSessionDto = this.login(username, password);
            hoursReportedResponse = getClient().getDayRegistry(comunytekSessionDto.getSessionId(), ComunytekConstants.ACTION_HOURS_REPORTED, comunytekSessionDto.getUsername(), comunytekSessionDto.getUsername(), dayFormatted);
        }

        LOG.debug("END getStatus");

        return ComunytekStatusDto.fromResponse(comunytekSessionDto.getUsername(), hoursReportedResponse, timestamp, getTimezone());
    }

    @CacheInvalidate(cacheName = Constants.Caches.COMUNYTEK_STATUS)
    public void invalidateStatus(final String username, final String password) {
        LOG.info("Invalidated status cache of user '{}'", username);
    }

    public ComunytekActionResponseDto executeAction(@CacheKey final String username, @CacheKey final String password, final ComunytekAction action) {
        LOG.debug("BEGIN executeAction: [{}] [{}] [{}]", username, Constants.HIDDEN_PASSWORD, action);

        ComunytekSessionDto comunytekSessionDto = this.login(username, password);

        String executeActionResponse = getClient().executeAction(comunytekSessionDto.getSessionId(), ComunytekConstants.ACTION_REPORT_HOURS, comunytekSessionDto.getUsername(), comunytekSessionDto.getUsername(), action.getCode(), action.getComment());

        if (ComunytekConstants.ERROR_EXPIRED_SESSION.equals(executeActionResponse)) {
            this.invalidateSession(comunytekSessionDto, username, password);
            comunytekSessionDto = this.login(username, password);
            executeActionResponse = getClient().executeAction(comunytekSessionDto.getSessionId(), ComunytekConstants.ACTION_REPORT_HOURS, comunytekSessionDto.getUsername(), comunytekSessionDto.getUsername(), action.getCode(), action.getComment());
        }

        LOG.debug("END executeAction");

        final ComunytekActionResponseDto response = ComunytekActionResponseDto.fromResponse(executeActionResponse);

        if (response.getResult()) {
            invalidateStatus(username, password);
        }

        return response;
    }

    private boolean isClientMocked() {
        return timehammerConfig.getMocks().isComunytekClientMocked();
    }

    private ComunytekRestClient getClient() {
        return isClientMocked() ? comunytekMockedClient : comunytekRealClient;
    }

    private SupportedTimezone getTimezone() {
        return SupportedTimezone.EUROPE_MADRID;
    }
}