package com.kronostools.timehammer.core;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.SaveHolidayResult;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidaysWorker;
import com.kronostools.timehammer.core.dao.WorkerHolidaysDao;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UpdateWorkersHolidaysWorkerProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateWorkersHolidaysWorkerProcessor.class);

    private final WorkerHolidaysDao workerHolidaysDao;

    public UpdateWorkersHolidaysWorkerProcessor(final WorkerHolidaysDao workerHolidaysDao) {
        this.workerHolidaysDao = workerHolidaysDao;
    }

    @Incoming(Channels.HOLIDAYS_WORKER_UPDATE)
    @Outgoing(Channels.HOLIDAYS_WORKER_SUMMARY)
    public Uni<Message<UpdateWorkersHolidaysWorker>> process(final Message<UpdateWorkersHolidaysWorker> message) {
        final UpdateWorkersHolidaysWorker worker = message.getPayload();

        if (worker.getCheckHolidayResult().isNotSuccessful()) {
            LOG.warn("Worker's holidays cannot be updated because it could not have been checked. Reason: {}", worker.getCheckHolidayResult().getErrorMessage());

            worker.setSaveHolidayResult(SaveHolidayResult.Builder.builder()
                    .buildUnsuccessful(worker.getCheckHolidayResult().getErrorMessage()));
        } else {
            if (worker.getCheckHolidayResult().getHoliday()) {
                workerHolidaysDao
                        .upsert(worker.getWorkerInternalId(), worker.getHolidayCandidate())
                        .onItem()
                        .invoke((upsertResult) -> {
                            final SaveHolidayResult result;

                            if (upsertResult.isSuccessful()) {
                                result = SaveHolidayResult.Builder.builder()
                                        .build();
                            } else {
                                result = SaveHolidayResult.Builder.builder()
                                        .buildUnsuccessful(upsertResult.getErrorMessage());
                            }

                            worker.setSaveHolidayResult(result);
                        });
            } else {
                worker.setSaveHolidayResult(SaveHolidayResult.Builder.builder().build());
            }
        }

        // TODO: revisar flujo, tal cual está no funcionaría sin esperar a que se ejecute la inserción en base de datos o sin cambiar el flujo
        return Uni.createFrom().item(Message.of(worker, message::ack));
    }
}