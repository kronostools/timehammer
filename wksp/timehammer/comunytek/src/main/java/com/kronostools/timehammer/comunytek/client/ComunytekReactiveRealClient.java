package com.kronostools.timehammer.comunytek.client;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.common.utils.CommonUtils;
import com.kronostools.timehammer.comunytek.config.LoginCacheConfig;
import com.kronostools.timehammer.comunytek.exception.ComunytekException;
import com.kronostools.timehammer.comunytek.exception.ComunytekExpiredSessionException;
import com.kronostools.timehammer.comunytek.exception.ComunytekIncorrectLoginException;
import com.kronostools.timehammer.comunytek.exception.ComunytekUnexpectedException;
import com.kronostools.timehammer.comunytek.model.ComunytekHolidayForm;
import com.kronostools.timehammer.comunytek.model.ComunytekHolidayResponse;
import com.kronostools.timehammer.comunytek.model.ComunytekLoginForm;
import com.kronostools.timehammer.comunytek.model.ComunytekLoginResponse;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.stream.Stream;

public class ComunytekReactiveRealClient implements ComunytekClient {
    private static final Logger LOG = LoggerFactory.getLogger(ComunytekReactiveRealClient.class);

    private static final String BASE_URL = "/SWHandler";

    private static final long UNEXPECTED_ERROR_MAX_RETRIES = 2L;
    private static final long EXPIRED_SESSION_MAX_RETRIES = 1L;

    private static final String OK = "OK";
    private static final String LINE_BREAK = "\n";
    private static final String TAB = "\t";

    private static final String ACTION_HOURS_REPORTED = "LISTRH";
    private static final String ACTION_REPORT_HOURS = "ADDRH";

    private static final String ERROR_EXPIRED_SESSION = "ERROR La sesion es invalida o ha expirado";

    private final WebClient client;

    private final Cache<String, ComunytekLoginResponse> cache;

    public ComunytekReactiveRealClient(final Vertx vertx, final LoginCacheConfig loginCacheConfig) {
        this.client = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost("empleados.comunytek.com")
                .setDefaultPort(443)
                .setSsl(true)
                .setTrustAll(true));

        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(loginCacheConfig.getExpiration().getQty(), loginCacheConfig.getExpiration().getUnit())
                .build();
    }

    public Uni<ComunytekLoginResponse> login(final String username, final String password) {
        LOG.debug("Trying to login user '{}' ...", username);

        final ComunytekLoginResponse loginResponse = cache.getIfPresent(username);

        if (loginResponse != null) {
            LOG.debug("Recovered login response from cache");

            return Uni.createFrom().item(loginResponse);
        } else {
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

                                throw new ComunytekIncorrectLoginException(message);
                            } else {
                                LOG.debug("User '{}' was logged in successfully", username);

                                final String[] responseParts = response.bodyAsString().split(LINE_BREAK);

                                result = ComunytekLoginResponse.Builder.builder()
                                        .fullname(responseParts[0])
                                        .sessionId(responseParts[2])
                                        .username(username)
                                        .build();

                                cache.put(username, result);
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
    }

    @Override
    public Uni<ComunytekHolidayResponse> isHoliday(final String username, final String password, final LocalDate holidayCandidate) {
        LOG.debug("Checking if '{}' is holiday for user '{}' ...", CommonDateTimeUtils.formatDateToLog(holidayCandidate), username);

        return login(username, password)
                .onFailure(ComunytekException.class)
                    .recoverWithItem((e) -> ComunytekLoginResponse.Builder.builder().buildUnsuccessful(e.getMessage()))
                .onItem()
                .produceUni(loginResponse -> {
                    if (loginResponse.isSuccessful()) {
                        LOG.debug("Calling comunytek to get holidays ...");

                        return client
                                .post(getUrl("regvac"))
                                .putHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
                                .sendBuffer(ComunytekHolidayForm.Builder.builder()
                                        .sessionId(loginResponse.getSessionId())
                                        .username(username)
                                        .build()
                                        .toBuffer())
                                .map(response -> {
                                    LOG.debug("Processing holidays response from comunytek - status: {}", response.statusCode());

                                    final ComunytekHolidayResponse result;

                                    if (response.statusCode() == 200) {
                                        if (ERROR_EXPIRED_SESSION.equals(response.bodyAsString())) {
                                            cache.invalidate(username);

                                            final String message = CommonUtils.stringFormat("Session of user '{}' has expired and its holidays couldn't be retrieve", username);
                                            LOG.warn(message);

                                            throw new ComunytekExpiredSessionException(message);
                                        } else {
                                            LOG.debug("Holidays of user '{}' were retrieved successfully", username);

                                            result = ComunytekHolidayResponse.Builder.builder()
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
                        return Uni.createFrom().item(ComunytekHolidayResponse.Builder.builder()
                                .buildUnsuccessful(loginResponse.getErrorMessage()));
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
}