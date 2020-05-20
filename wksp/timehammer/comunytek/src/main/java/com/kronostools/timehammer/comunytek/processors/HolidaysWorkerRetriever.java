package com.kronostools.timehammer.comunytek.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.CheckHolidayPhase;
import com.kronostools.timehammer.common.messages.schedules.CheckHolidayPhaseBuilder;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorker;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorkerBuilder;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.constants.CredentialsResponse;
import com.kronostools.timehammer.comunytek.exception.ComunytekUnexpectedException;
import com.kronostools.timehammer.comunytek.model.ComunytekHolidayResponse;
import com.kronostools.timehammer.comunytek.service.WorkerCredentialsService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HolidaysWorkerRetriever {
    private static final Logger LOG = LoggerFactory.getLogger(HolidaysWorkerRetriever.class);

    private final WorkerCredentialsService workerCredentialsService;
    private final ComunytekClient comunytekClient;

    public HolidaysWorkerRetriever(final WorkerCredentialsService workerCredentialsService,
                                   final ComunytekClient comunytekClient) {
        this.workerCredentialsService = workerCredentialsService;
        this.comunytekClient = comunytekClient;
    }

    @Incoming(Channels.COMUNYTEK_WORKER_HOLIDAYS)
    @Outgoing(Channels.HOLIDAYS_WORKER_UPDATE)
    public Uni<Message<UpdateWorkersHolidayWorker>> retrieveHolidays(final Message<UpdateWorkersHolidayWorker> message) {
        final UpdateWorkersHolidayWorker worker = UpdateWorkersHolidayWorkerBuilder.copy(message.getPayload()).build();

        return workerCredentialsService.getWorkerCredentials(worker.getWorkerInternalId())
                .flatMap(workerCredentialsResponse -> {
                    if (workerCredentialsResponse.isSuccessful()) {
                        LOG.info("Checking if worker '{}' working at '{}' picked '{}' as holiday ...", worker.getWorkerInternalId(), worker.getCompany().getCode(), CommonDateTimeUtils.formatDateToLog(worker.getHolidayCandidate()));

                        return comunytekClient
                                .isHoliday(worker.getWorkerExternalId(), workerCredentialsResponse.getExternalPassword(), worker.getHolidayCandidate())
                                .onFailure(ComunytekUnexpectedException.class)
                                    .recoverWithItem((e) -> ComunytekHolidayResponse.Builder.builder().buildUnsuccessful(e.getMessage()))
                                .map(holidayResponse -> {
                                    final CheckHolidayPhase checkHolidayPhase;

                                    if (holidayResponse.isSuccessful()) {
                                        LOG.info("Worker '{}' working at '{}' {} '{}' as holiday", worker.getWorkerInternalId(), worker.getCompany().getCode(), holidayResponse.isHoliday() ? "picked" : "didn't pick", CommonDateTimeUtils.formatDateToLog(worker.getHolidayCandidate()));

                                        checkHolidayPhase = new CheckHolidayPhaseBuilder()
                                                .holiday(holidayResponse.isHoliday())
                                                .build();
                                    } else {
                                        LOG.warn("Holidays of worker '{}' couldn't be retrieved", worker.getWorkerInternalId());

                                        checkHolidayPhase = new CheckHolidayPhaseBuilder()
                                                .errorMessage(holidayResponse.getErrorMessage())
                                                .build();
                                    }

                                    worker.setCheckHolidayPhase(checkHolidayPhase);

                                    return Message.of(worker, message::ack);
                                });
                    } else {
                        String errorMessage = null;

                        if (workerCredentialsResponse.getResponse() == CredentialsResponse.EXPIRED) {
                            errorMessage = "Worker's external password is expired";
                        } else if (workerCredentialsResponse.getResponse() == CredentialsResponse.NOT_FOUND) {
                            errorMessage = "Worker's external password is missing";
                        }

                        LOG.warn("Worker's holidays cannot be checked. Reason: {}", errorMessage);

                        worker.setCheckHolidayPhase(new CheckHolidayPhaseBuilder()
                                .errorMessage(errorMessage)
                                .build());

                        return Uni.createFrom()
                                .item(Message.of(worker, message::ack));
                    }
                });
    }
}