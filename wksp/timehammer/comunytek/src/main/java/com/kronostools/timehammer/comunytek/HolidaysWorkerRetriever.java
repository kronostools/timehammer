package com.kronostools.timehammer.comunytek;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.CheckHolidayResult;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidaysWorker;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.exception.ComunytekUnexpectedException;
import com.kronostools.timehammer.comunytek.model.ComunytekHolidayResponse;
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

    private final ComunytekClient comunytekClient;

    public HolidaysWorkerRetriever(final ComunytekClient comunytekClient) {
        this.comunytekClient = comunytekClient;
    }

    @Incoming(Channels.COMUNYTEK_WORKER_HOLIDAYS)
    @Outgoing(Channels.HOLIDAYS_WORKER_UPDATE)
    public Uni<Message<UpdateWorkersHolidaysWorker>> retrieveHolidays(final Message<UpdateWorkersHolidaysWorker> message) {
        final UpdateWorkersHolidaysWorker worker = message.getPayload();

        if (worker.getCredentialResult().isNotSuccessful()) {
            LOG.warn("Worker's holidays cannot be checked because worker's credentials are missing. Reason: {}", worker.getCredentialResult().getErrorMessage());

            worker.setCheckHolidayResult(CheckHolidayResult.Builder.builder()
                    .buildUnsuccessful(worker.getCredentialResult().getErrorMessage()));

            return Uni.createFrom().item(Message.of(worker, message::ack));
        } else {
            LOG.info("Checking if worker '{}' working at '{}' picked '{}' as holiday ...", worker.getWorkerInternalId(), worker.getCompany().getCode(), CommonDateTimeUtils.formatDateToLog(worker.getHolidayCandidate()));

            return comunytekClient
                    .isHoliday(worker.getWorkerExternalId(), worker.getCredentialResult().getExternalPassword(), worker.getHolidayCandidate())
                    .onFailure(ComunytekUnexpectedException.class)
                        .recoverWithItem((e) -> ComunytekHolidayResponse.Builder.builder().buildUnsuccessful(e.getMessage()))
                    .map((holidayResponse) -> {
                        if (holidayResponse.isSuccessful()) {
                            LOG.info("Worker '{}' working at '{}' {} '{}' as holiday", worker.getWorkerInternalId(), worker.getCompany().getCode(), holidayResponse.isHoliday() ? "picked" : "didn't pick", CommonDateTimeUtils.formatDateToLog(worker.getHolidayCandidate()));

                            worker.setCheckHolidayResult(CheckHolidayResult.Builder.builder()
                                    .holiday(holidayResponse.isHoliday())
                                    .build());
                        } else {
                            LOG.warn("Holidays of worker '{}' couldn't be retrieved", worker.getWorkerInternalId());

                            worker.setCheckHolidayResult(CheckHolidayResult.Builder.builder()
                                    .buildUnsuccessful(holidayResponse.getErrorMessage()));
                        }

                        return Message.of(worker, message::ack);
                    });
        }
    }
}