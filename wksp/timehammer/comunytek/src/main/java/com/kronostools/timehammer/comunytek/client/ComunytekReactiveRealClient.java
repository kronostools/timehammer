package com.kronostools.timehammer.comunytek.client;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.comunytek.config.LoginCacheConfig;
import com.kronostools.timehammer.comunytek.model.ComunytekLoginResponse;
import com.kronostools.timehammer.comunytek.model.HolidayResponse;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.stream.Stream;

public class ComunytekReactiveRealClient implements ComunytekClient {
    private static final Logger LOG = LoggerFactory.getLogger(ComunytekReactiveRealClient.class);

    private static final String FAKE_SESSIONID = "11111111";
    private static final String S = "S";
    private static final String N = "N";
    private static final String OK = "OK";
    private static final String LINE_BREAK = "\n";
    private static final String TAB = "\t";

    private static final String ACTION_LOGIN = "LOGIN";
    private static final String ACTION_HOLIDAYS = "LISTVAC";
    private static final String ACTION_HOURS_REPORTED = "LISTRH";
    private static final String ACTION_REPORT_HOURS = "ADDRH";

    private static final String ERROR_EXPIRED_SESSION = "ERROR La sesion es invalida o ha expirado";

    private final WebClient client;

    private final Cache<String, ComunytekLoginResponse> cache;

    public ComunytekReactiveRealClient(final Vertx vertx, final LoginCacheConfig loginCacheConfig) {
        this.client = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost("empleados.comunytek.com/SWHandler")
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

            return client.post("/selfweb")
                    .sendForm(MultiMap.caseInsensitiveMultiMap()
                            .add("sessionId", FAKE_SESSIONID)
                            .add("par_1", ACTION_LOGIN)
                            .add("par_2", username)
                            .add("par_3", password)
                            .add("par_4", S))
                    .map(response -> {
                        LOG.debug("Processing login response from comunytek - status: {}", response.statusCode());

                        final ComunytekLoginResponse result = new ComunytekLoginResponse();

                        if (response.statusCode() == 200) {
                            if (response.bodyAsString().startsWith("ERROR")) {
                                LOG.warn("There was an error while login, incorrect username ({}) or password (*****)", username);

                                result.setSuccessful(false);
                            } else {
                                LOG.debug("User '{}' was logged in successfully", username);

                                final String[] responseParts = response.bodyAsString().split(LINE_BREAK);

                                result.setSuccessful(true);
                                result.setFullname(responseParts[0]);
                                result.setSessionId(responseParts[2]);
                                result.setUsername(username);

                                cache.put(username, result);
                            }
                        } else {
                            LOG.error("There was an unexpected error trying to login user '{}'", username);

                            result.setSuccessful(false);
                        }

                        return result;
                    });
        }
    }

    @Override
    public Uni<HolidayResponse> isHoliday(final String username, final String password, final LocalDate date) {
        LOG.debug("Checking if '{}' is holiday for user '{}' ...", CommonDateTimeUtils.formatDateToLog(date), username);

        return login(username, password)
                .onItem()
                .produceUni(clr -> {
                    if (clr.isSuccessful()) {
                        LOG.debug("Calling comunytek to get holidays ...");

                        return client
                                .post("/regvac")
                                .sendForm(MultiMap.caseInsensitiveMultiMap()
                                        .add("sessionId", clr.getSessionId())
                                        .add("par_1", ACTION_HOLIDAYS)
                                        .add("par_2", username)
                                        .add("par_3", username)
                                        .add("par_4", N))
                                .map(response -> {
                                    LOG.debug("Processing holidays response from comunytek - status: {}", response.statusCode());

                                    final HolidayResponse result = new HolidayResponse();

                                    if (response.statusCode() == 200) {
                                        if (ERROR_EXPIRED_SESSION.equals(response.bodyAsString())) {
                                            LOG.warn("Session of user '{}' has expired and its holidays couldn't be retrieve", username);

                                            result.setSuccessful(false);
                                        } else {
                                            LOG.debug("Holidays of user '{}' were retrieved successfully", username);

                                            result.setSuccessful(true);
                                            result.setHoliday(Stream.of(response.bodyAsString().split(LINE_BREAK))
                                                    .map(rawHoliday -> rawHoliday.split(TAB))
                                                    .map(holidayParts -> CommonDateTimeUtils.parseDateFromComunytek(holidayParts[0]))
                                                    .anyMatch(d -> d.equals(date)));

                                            LOG.debug("User '{}' {} '{}' as holiday", username, result.isHoliday() ? "picked" : "didn't pick", CommonDateTimeUtils.formatDateToLog(date));
                                        }
                                    } else {
                                        LOG.error("There was an unexpected error trying to login user '{}'", username);

                                        result.setSuccessful(false);
                                    }

                                    return result;
                                });
                    } else {
                        return Uni.createFrom().item(() -> {
                            final HolidayResponse result = new HolidayResponse();
                            result.setSuccessful(false);

                            return result;
                        });
                    }
                });
    }

    @Override
    public boolean isMocked() {
        return false;
    }
}