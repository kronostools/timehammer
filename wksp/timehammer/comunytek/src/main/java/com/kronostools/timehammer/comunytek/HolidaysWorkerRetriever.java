package com.kronostools.timehammer.comunytek;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidaysWorker;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;

@ApplicationScoped
public class HolidaysWorkerRetriever {
    private static final Logger LOG = LoggerFactory.getLogger(HolidaysWorkerRetriever.class);

    /*
    private final ComunytekClient comunytekClient;

    public HolidaysWorkerRetriever(final ComunytekClient comunytekClient) {
        this.comunytekClient = comunytekClient;
    }
    */

    private final WebClient client;

    public HolidaysWorkerRetriever(final Vertx vertx) {
        this.client = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost("reqbin.com")
                .setDefaultPort(443)
                .setSsl(true)
                .setTrustAll(true));
    }

    private Uni<String> nested() {
        LOG.info("--- NESTED CALL ---");

        return client.post("/echo/post/form")
                .putHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
                .sendForm(MultiMap.caseInsensitiveMultiMap()
                        .add("key", "value"))
                .flatMap((response) -> {
                    String result = null;

                    if (response.statusCode() == 200) {
                        if (response.bodyAsString().startsWith("ERROR")) {
                            LOG.warn("There was an error while login user 'DCV': {}", response.bodyAsString());
                        } else {
                            LOG.info("User 'DCV' was logged in successfully");

                            final String[] responseParts = response.bodyAsString().split("\n");

                            result = responseParts[2];
                        }
                    } else {
                        LOG.warn("NESTED - Error while making http request");
                    }

                    return Uni.createFrom().item(result);
                });
    }

    private Uni<Boolean> outer() {
        LOG.info("--- OUTER CALL ---");

        return nested()
                .onItem()
                .produceUni((sessionId) -> {
                    if (sessionId != null) {
                        return client
                                .post("/echo/post/form")
                                .putHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
                                .sendForm(MultiMap.caseInsensitiveMultiMap()
                                        .add("sessionId", sessionId))
                                .flatMap((resp) -> {
                                    final Boolean result;

                                    if (resp.statusCode() == 200) {
                                        LOG.info("OUTER - Successful response (session: {}) - text: {}", sessionId, resp.bodyAsString());

                                        result = Boolean.TRUE;
                                    } else {
                                        LOG.warn("OUTER - Error while making http request");

                                        result = Boolean.FALSE;
                                    }

                                    return Uni.createFrom().item(result);
                                });
                    } else {
                        LOG.warn("OUTER - Error while calling nested");

                        return Uni.createFrom().item(false);
                    }
        });
    }

    @Incoming(Channels.COMUNYTEK_WORKER_HOLIDAYS)
    @Outgoing(Channels.HOLIDAYS_WORKER_UPDATE)
    public Uni<Message<Boolean>> retrieveHolidays(final Message<UpdateWorkersHolidaysWorker> message) {
        final UpdateWorkersHolidaysWorker worker = message.getPayload();

        final LocalDate holidayCandidate = worker.getTimestamp().toLocalDate();

        LOG.info("Checking if worker '{}' working at '{}' picked '{}' as holiday ...", worker.getWorkerInternalId(), worker.getCompany().getCode(), CommonDateTimeUtils.formatDateToLog(holidayCandidate));

        return outer().flatMap((response) -> {
            if (response) {
                LOG.info("MAIN - successful call");
            } else {
                LOG.info("MAIN - erroneous call");
            }

            return Uni.createFrom().item(Message.of(response, message::ack));
        });
    }

    /*
    @Incoming(Channels.COMUNYTEK_WORKER_HOLIDAYS)
    @Outgoing(Channels.HOLIDAYS_WORKER_UPDATE)
    public Uni<Message<UpdateWorkersHolidaysWorker>> retrieveHolidays(final Message<UpdateWorkersHolidaysWorker> message) {
        final UpdateWorkersHolidaysWorker worker = message.getPayload();

        if (worker.getExternalPassword() == null) {
            return Uni.createFrom().item(Message.of(worker, message::ack));
        } else {
            final LocalDate holidayCandidate = worker.getTimestamp().toLocalDate();

            LOG.info("Checking if worker '{}' working at '{}' picked '{}' as holiday ...", worker.getWorkerInternalId(), worker.getCompany().getCode(), CommonDateTimeUtils.formatDateToLog(holidayCandidate));

            return comunytekClient
                    .isHoliday(worker.getWorkerExternalId(), worker.getExternalPassword(), holidayCandidate)
                    .onFailure(ComunytekUnexpectedException.class)
                        .recoverWithItem(HolidayResponse.Builder.buildUnsuccessful())
                    .map((holidayResponse) -> {
                        if (holidayResponse.isSuccessful()) {
                            LOG.info("Worker '{}' working at '{}' {} '{}' as holiday", worker.getWorkerInternalId(), worker.getCompany().getCode(), holidayResponse.isHoliday() ? "picked" : "didn't pick", CommonDateTimeUtils.formatDateToLog(holidayCandidate));

                            if (holidayResponse.isHoliday()) {
                                worker.addHoliday(holidayResponse.getDate());
                            }
                        } else {
                            LOG.warn("Holidays of worker '{}' couldn't be retrieved", worker.getWorkerInternalId());

                            worker.setUpdatedSuccessfully(Boolean.FALSE);
                        }

                        return Message.of(worker, message::ack);
                    });
        }
    }
    */
}