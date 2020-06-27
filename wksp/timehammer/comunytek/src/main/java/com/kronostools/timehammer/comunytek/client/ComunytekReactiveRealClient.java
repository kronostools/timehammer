package com.kronostools.timehammer.comunytek.client;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.common.utils.CommonUtils;
import com.kronostools.timehammer.comunytek.config.LoginCacheConfig;
import com.kronostools.timehammer.comunytek.constants.*;
import com.kronostools.timehammer.comunytek.exception.ComunytekExpiredSessionException;
import com.kronostools.timehammer.comunytek.exception.ComunytekUnexpectedException;
import com.kronostools.timehammer.comunytek.model.*;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

public class ComunytekReactiveRealClient extends AbstractComunytekClient {
    private static final String BASE_URL = "/SWHandler";

    private static final long UNEXPECTED_ERROR_MAX_RETRIES = 2L;
    private static final long EXPIRED_SESSION_MAX_RETRIES = 2L;

    private static final String OK = "OK";
    private static final String LINE_BREAK = "\n";
    private static final String TAB = "\t";

    private static final String ACTION_HOURS_REPORTED = "LISTRH";
    private static final String ACTION_REPORT_HOURS = "ADDRH";

    private static final String ERROR_EXPIRED_SESSION = "ERROR La sesion es invalida o ha expirado";

    private final WebClient client;

    private final Cache<String, ComunytekCachedLoginResponse> loginCache;

    private final TimeMachineService timeMachineService;

    public ComunytekReactiveRealClient(final Vertx vertx,
                                       final LoginCacheConfig loginCacheConfig,
                                       final TimeMachineService timeMachineService) {
        this.client = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost("empleados.comunytek.com")
                .setDefaultPort(443)
                .setSsl(true)
                .setTrustAll(true));

        this.loginCache = Caffeine.newBuilder()
                .expireAfterWrite(loginCacheConfig.getExpiration().getQty(), loginCacheConfig.getExpiration().getUnit())
                .build();

        this.timeMachineService = timeMachineService;
    }

    @Override
    public Uni<ComunytekLoginResponse> login(final String username, final String password) {
        LOG.debug("Calling comunytek to login ...");

        return client.post(getUrl("selfweb"))
                .putHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
                .sendBuffer(ComunytekLoginForm.Builder.builder()
                        .username(username)
                        .password(password)
                        .build()
                        .toBuffer())
                .map(response -> {
                    LOG.debug("Processing login response from comunytek - status: {}", response.statusCode());

                    final ComunytekLoginResponse result;

                    if (response.statusCode() == 200) {
                        if (response.bodyAsString().startsWith("ERROR")) {
                            final String message = CommonUtils.stringFormat("There was an error while login, incorrect username ({}) or password (*****)", username);
                            LOG.warn(message);

                            result = new ComunytekLoginResponseBuilder()
                                    .result(ComunytekLoginResult.INVALID)
                                    .build();

                            markCredentialsAsInvalid(username);
                        } else {
                            LOG.debug("User '{}' was logged in successfully", username);

                            final String[] responseParts = response.bodyAsString().split(LINE_BREAK);

                            result = new ComunytekLoginResponseBuilder()
                                    .result(ComunytekLoginResult.OK)
                                    .fullname(responseParts[0])
                                    .sessionId(responseParts[2])
                                    .username(username)
                                    .build();

                            updateCredentials(username, password);
                            updateSession(username, result.getSessionId(), result.getFullname());
                        }
                    } else {
                        final String message = CommonUtils.stringFormat("There was an unexpected error trying to login user '{}'", username);
                        LOG.error(message);

                        throw new ComunytekUnexpectedException(message);
                    }

                    return result;
                })
                .onFailure(ComunytekUnexpectedException.class)
                    .retry()
                    .atMost(UNEXPECTED_ERROR_MAX_RETRIES);
    }

    private Uni<ComunytekCachedLoginResponse> cachedLogin(final String username) {
        LOG.debug("Trying to login user '{}' ...", username);

        final ComunytekCachedLoginResponse loginResponse = loginCache.getIfPresent(username);

        if (loginResponse != null) {
            LOG.debug("Recovered login response from cache");

            return Uni.createFrom().item(loginResponse);
        } else {
            final CachedWorkerCredentials cachedWorkerCredentials = credentialsCache.getIfPresent(username);

            if (cachedWorkerCredentials == null) {
                final String errorMessage = CommonUtils.stringFormat("Credentials of user '{}' are missing", username);

                LOG.warn(errorMessage);

                return Uni.createFrom().item(new ComunytekCachedLoginResponseBuilder()
                        .result(ComunytekCachedLoginResult.MISSING)
                        .errorMessage(errorMessage)
                        .build());
            } else if (cachedWorkerCredentials.isInvalid()) {
                final String errorMessage = CommonUtils.stringFormat("Last login (on {}) with registered credentials of user '{}' was unsuccessful", CommonDateTimeUtils.formatDateTimeToLog(cachedWorkerCredentials.getInvalidSince()), username);

                LOG.warn(errorMessage);

                return Uni.createFrom().item(new ComunytekCachedLoginResponseBuilder()
                        .result(ComunytekCachedLoginResult.INVALID)
                        .errorMessage(errorMessage)
                        .build());
            } else {
                LOG.debug("Calling comunytek to login ...");

                return login(username, cachedWorkerCredentials.getExternalPassword())
                        .onFailure(Exception.class)
                            .recoverWithItem(e -> new ComunytekLoginResponseBuilder()
                                    .result(ComunytekLoginResult.KO)
                                    .errorMessage(e.getMessage())
                                    .build())
                        .map(ComunytekCachedLoginResponseBuilder::copyAndBuild);
            }
        }
    }

    @Override
    public Uni<ComunytekHolidayResponse> isHoliday(final String username, final LocalDate holidayCandidate) {
        LOG.debug("Checking if '{}' is holiday for user '{}' ...", CommonDateTimeUtils.formatDateToLog(holidayCandidate), username);

        return cachedLogin(username)
                .flatMap(clr -> {
                    if (clr.isSuccessful()) {
                        LOG.debug("Calling comunytek to get holidays ...");

                        return client
                                .post(getUrl("regvac"))
                                .putHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
                                .sendBuffer(ComunytekHolidayForm.Builder.builder()
                                        .sessionId(clr.getSessionId())
                                        .username(username)
                                        .build()
                                        .toBuffer())
                                .map(response -> {
                                    LOG.debug("Processing holidays response from comunytek - status: {}", response.statusCode());

                                    final ComunytekHolidayResponse result;

                                    if (response.statusCode() == 200) {
                                        if (ERROR_EXPIRED_SESSION.equals(response.bodyAsString())) {
                                            invalidateSession(username);

                                            final String message = CommonUtils.stringFormat("Session of user '{}' has expired and its holidays couldn't be retrieved", username);
                                            LOG.warn(message);

                                            throw new ComunytekExpiredSessionException(message);
                                        } else {
                                            LOG.debug("Holidays of user '{}' were retrieved successfully", username);

                                            result = new ComunytekHolidayResponseBuilder()
                                                    .result(ComunytekSimpleResult.OK)
                                                    .date(holidayCandidate)
                                                    .holiday(Stream.of(response.bodyAsString().split(LINE_BREAK))
                                                        .map(rawHoliday -> rawHoliday.split(TAB))
                                                        .map(holidayParts -> CommonDateTimeUtils.parseDateFromComunytek(holidayParts[0]))
                                                        .anyMatch(d -> d.equals(holidayCandidate)))
                                                    .build();

                                            LOG.debug("User '{}' {} '{}' as holiday", username, result.isHoliday() ? "picked" : "didn't pick", CommonDateTimeUtils.formatDateToLog(holidayCandidate));
                                        }
                                    } else {
                                        final String message = CommonUtils.stringFormat("There was an unexpected error trying to get holidays of user '{}'", username);
                                        LOG.error(message);

                                        throw new ComunytekUnexpectedException(message);
                                    }

                                    return result;
                                })
                                .onFailure(ComunytekUnexpectedException.class)
                                    .retry()
                                    .atMost(UNEXPECTED_ERROR_MAX_RETRIES);
                    } else {
                        return Uni.createFrom().item(new ComunytekHolidayResponseBuilder()
                                .result(ComunytekSimpleResult.KO)
                                .errorMessage(clr.getErrorMessage())
                                .build());
                    }
                })
                .onFailure(ComunytekExpiredSessionException.class)
                    .retry()
                    .atMost(EXPIRED_SESSION_MAX_RETRIES);
    }

    @Override
    public Uni<ComunytekStatusResponse> getStatus(final String username, final LocalDateTime timestamp) {
        LOG.debug("Getting status of user '{}' ...", username);

        return cachedLogin(username)
                .flatMap(clr -> {
                    if (clr.isSuccessful()) {
                        LOG.debug("Calling comunytek to get status ...");

                        return client
                                .post(getUrl("reghoras"))
                                .putHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
                                .sendBuffer(ComunytekStatusForm.Builder.builder()
                                        .sessionId(clr.getSessionId())
                                        .username(username)
                                        .day(CommonDateTimeUtils.formatDateTimeToComunytek(timestamp))
                                        .build()
                                        .toBuffer())
                                .map(response -> {
                                    LOG.debug("Processing status response from comunytek - status: {}", response.statusCode());

                                    final ComunytekStatusResponse result;

                                    if (response.statusCode() == 200) {
                                        if (ERROR_EXPIRED_SESSION.equals(response.bodyAsString())) {
                                            invalidateSession(username);

                                            final String message = CommonUtils.stringFormat("Session of user '{}' has expired and its status couldn't be retrieved", username);
                                            LOG.warn(message);

                                            throw new ComunytekExpiredSessionException(message);
                                        } else {
                                            LOG.debug("Status of user '{}' were retrieved successfully", username);

                                            if (response.bodyAsString() != null && response.bodyAsString().length() > 0) {
                                                final String[] hoursReportedParts = response.bodyAsString().split(LINE_BREAK);

                                                result = Stream.of(hoursReportedParts)
                                                        .skip(hoursReportedParts.length - 1)
                                                        .findFirst()
                                                        .map(rawReport -> rawReport.split(TAB))
                                                        .map(hourReportParts -> {
                                                            // 26/03/2020\t13:21:43.012\tP\tPausa\t05.76\tComida
                                                            final LocalDate date = CommonDateTimeUtils.parseDateFromComunytek(hourReportParts[0]);
                                                            final LocalTime time = CommonDateTimeUtils.parseTimeFromComunytek(hourReportParts[1]);
                                                            final ComunytekStatusValue status = ComunytekStatusValue.fromCode(hourReportParts[2]);
                                                            final String workedTime = hourReportParts[4];
                                                            final String comment = hourReportParts[5];

                                                            return new ComunytekStatusResponseBuilder()
                                                                    .result(ComunytekStatusResult.OK)
                                                                    .date(date)
                                                                    .time(time)
                                                                    .status(status)
                                                                    .workedTime(workedTime)
                                                                    .comment(comment != null ? comment.trim() : "")
                                                                    .build();
                                                        })
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

                                            LOG.debug("User '{}' is in status '{}' ('{}')", username, result.getStatus().getCode(), result.getStatus().getText());
                                        }
                                    } else {
                                        final String message = CommonUtils.stringFormat("There was an unexpected error trying to get status of user '{}'", username);
                                        LOG.error(message);

                                        throw new ComunytekUnexpectedException(message);
                                    }

                                    return result;
                                })
                                .onFailure(ComunytekUnexpectedException.class)
                                    .retry()
                                    .atMost(UNEXPECTED_ERROR_MAX_RETRIES);
                    } else {
                        if (clr.getResult() == ComunytekCachedLoginResult.INVALID
                                || clr.getResult() == ComunytekCachedLoginResult.MISSING) {
                            return Uni.createFrom().item(new ComunytekStatusResponseBuilder()
                                    .result(ComunytekStatusResult.MISSING_OR_INVALID_CREDENTIALS)
                                    .errorMessage(clr.getErrorMessage())
                                    .build());
                        } else {
                            return Uni.createFrom().item(new ComunytekStatusResponseBuilder()
                                    .result(ComunytekStatusResult.KO)
                                    .errorMessage(clr.getErrorMessage())
                                    .build());
                        }
                    }
                })
                .onFailure(ComunytekExpiredSessionException.class)
                    .retry()
                    .atMost(EXPIRED_SESSION_MAX_RETRIES);
    }

    @Override
    public boolean isMocked() {
        return false;
    }

    private String getUrl(final String urlPart) {
        return CommonUtils.stringFormat("{}/{}", BASE_URL, urlPart);
    }

    private void invalidateSession(final String username) {
        loginCache.invalidate(username);
    }

    private void updateSession(final String username, final String sessionId, final String fullName) {
        loginCache.put(username, new ComunytekCachedLoginResponseBuilder()
                .result(ComunytekCachedLoginResult.OK)
                .username(username)
                .sessionId(sessionId)
                .fullname(fullName)
                .build());
    }

    private void updateCredentials(final String username, final String externalPassword) {
        credentialsCache.put(username, new CachedWorkerCredentialsBuilder()
                .invalid(false)
                .externalPassword(externalPassword)
                .build());
    }

    private void markCredentialsAsInvalid(final String username) {
        final CachedWorkerCredentials foundCredentials = credentialsCache.getIfPresent(username);

        if (foundCredentials != null) {
            credentialsCache.put(username, CachedWorkerCredentialsBuilder.copy(foundCredentials)
                    .invalid(true)
                    .invalidSince(timeMachineService.getNow())
                    .build());
        }
    }
}