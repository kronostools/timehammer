package com.kronostools.timehammer.comunytek;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidaysWorker;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;

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

        if (worker.getExternalPassword() == null) {
            return Uni.createFrom().item(Message.of(worker, message::ack));
        } else {
            final LocalDate holidayCandidate = worker.getTimestamp().toLocalDate();

            LOG.info("Checking if worker '{}' working at '{}' picked '{}' as holiday ...", worker.getWorkerInternalId(), worker.getCompany().getCode(), CommonDateTimeUtils.formatDateToLog(holidayCandidate));

            return comunytekClient
                    .isHoliday(worker.getWorkerExternalId(), worker.getExternalPassword(), holidayCandidate)
                    .map((holidayResponse) -> {
                        if (holidayResponse.isSuccessful()) {
                            LOG.info("Worker '{}' working at '{}' {} '{}' as holiday", worker.getWorkerInternalId(), worker.getCompany().getCode(), holidayResponse.isHoliday() ? "picked" : "didn't pick", CommonDateTimeUtils.formatDateToLog(holidayCandidate));

                            if (holidayResponse.isHoliday()) {
                                worker.addHoliday(holidayCandidate);
                            }
                        } else {
                            LOG.warn("Holidays of worker '{}' couldn't be retrieved", worker.getWorkerInternalId());

                            worker.setUpdatedSuccessfully(Boolean.FALSE);
                        }

                        return Message.of(worker, message::ack);
                    });
        }
    }
}