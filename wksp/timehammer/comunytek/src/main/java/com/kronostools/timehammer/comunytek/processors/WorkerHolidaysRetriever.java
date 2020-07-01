package com.kronostools.timehammer.comunytek.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;
import com.kronostools.timehammer.common.messages.schedules.CheckHolidayPhase;
import com.kronostools.timehammer.common.messages.schedules.CheckHolidayPhaseBuilder;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorker;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorkerBuilder;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.constants.ComunytekSimpleResult;
import com.kronostools.timehammer.comunytek.model.ComunytekHolidayResponseBuilder;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerHolidaysRetriever {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerHolidaysRetriever.class);

    private final ComunytekClient comunytekClient;

    public WorkerHolidaysRetriever(final ComunytekClient comunytekClient) {
        this.comunytekClient = comunytekClient;
    }

    @Incoming(Channels.COMUNYTEK_WORKER_HOLIDAYS)
    @Outgoing(Channels.HOLIDAYS_WORKER_UPDATE)
    public Uni<Message<UpdateWorkersHolidayWorker>> retrieveHolidays(final Message<UpdateWorkersHolidayWorker> message) {
        final UpdateWorkersHolidayWorker worker = UpdateWorkersHolidayWorkerBuilder.copy(message.getPayload()).build();

        LOG.info("Getting holidays of worker '{}' from Comunytek ...", worker.getWorkerInternalId());

        return comunytekClient
                .isHoliday(worker.getWorkerExternalId(), worker.getHolidayCandidate())
                .onFailure(Exception.class)
                    .recoverWithItem((e) -> new ComunytekHolidayResponseBuilder()
                            .result(ComunytekSimpleResult.KO)
                            .errorMessage(e.getMessage())
                            .build())
                .map(holidayResponse -> {
                    final CheckHolidayPhase checkHolidayPhase;

                    if (holidayResponse.isSuccessful()) {
                        LOG.info("Worker '{}' working at '{}' {} '{}' as holiday", worker.getWorkerInternalId(), worker.getCompany().getCode(), holidayResponse.getHoliday() ? "picked" : "didn't pick", CommonDateTimeUtils.formatDateToLog(worker.getHolidayCandidate()));

                        checkHolidayPhase = new CheckHolidayPhaseBuilder()
                                .result(SimpleResult.OK)
                                .holiday(holidayResponse.getHoliday())
                                .build();
                    } else {
                        LOG.warn("Holidays of worker '{}' couldn't be retrieved", worker.getWorkerInternalId());

                        checkHolidayPhase = new CheckHolidayPhaseBuilder()
                                .result(SimpleResult.KO)
                                .errorMessage(holidayResponse.getErrorMessage())
                                .build();
                    }

                    worker.setCheckHolidayPhase(checkHolidayPhase);

                    return Message.of(worker, message::ack);
                });
    }
}