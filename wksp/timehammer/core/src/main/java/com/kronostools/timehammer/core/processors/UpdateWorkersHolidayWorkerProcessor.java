package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.SaveHolidayPhase;
import com.kronostools.timehammer.common.messages.schedules.SaveHolidayPhaseBuilder;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorker;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorkerBuilder;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.core.dao.WorkerHolidaysDao;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UpdateWorkersHolidayWorkerProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateWorkersHolidayWorkerProcessor.class);

    private final WorkerHolidaysDao workerHolidaysDao;

    public UpdateWorkersHolidayWorkerProcessor(final WorkerHolidaysDao workerHolidaysDao) {
        this.workerHolidaysDao = workerHolidaysDao;
    }

    @Incoming(Channels.HOLIDAYS_WORKER_UPDATE)
    @Outgoing(Channels.HOLIDAYS_WORKER_SUMMARY)
    public Uni<Message<UpdateWorkersHolidayWorker>> process(final Message<UpdateWorkersHolidayWorker> message) {
        final UpdateWorkersHolidayWorker worker = UpdateWorkersHolidayWorkerBuilder.copy(message.getPayload()).build();

        LOG.info("Trying to update holidays of worker '{}' ...", worker.getWorkerInternalId());

        if (worker.getCheckHolidayPhase().isNotSuccessful()) {
            LOG.warn("Worker's holidays cannot be updated because it could not have been checked. Reason: {}", worker.getCheckHolidayPhase().getErrorMessage());

            worker.setSaveHolidayPhase(new SaveHolidayPhaseBuilder()
                    .errorMessage(worker.getCheckHolidayPhase().getErrorMessage())
                    .build());

            return Uni.createFrom().item(Message.of(worker, message::ack));
        } else {
            if (worker.getCheckHolidayPhase().isHoliday()) {
                return workerHolidaysDao
                        .upsert(worker.getWorkerInternalId(), worker.getHolidayCandidate())
                        .onItem()
                        .produceUni((upsertResult) -> {
                            final SaveHolidayPhase saveHolidayPhase;

                            if (upsertResult.isSuccessful()) {
                                LOG.info("Holidays of worker '{}' were updated successfully", worker.getWorkerInternalId());

                                saveHolidayPhase = new SaveHolidayPhaseBuilder()
                                        .build();
                            } else {
                                LOG.error("Unexpected error while updating holidays of worker '{}'. Error: {}", worker.getWorkerInternalId(), upsertResult.getErrorMessage());

                                saveHolidayPhase = new SaveHolidayPhaseBuilder()
                                        .errorMessage(upsertResult.getErrorMessage())
                                        .build();
                            }

                            worker.setSaveHolidayPhase(saveHolidayPhase);

                            return Uni.createFrom().item(Message.of(worker, message::ack));
                        });
            } else {
                LOG.info("Nothing to update because worker '{}' didn't picked '{}' as holidays", worker.getWorkerInternalId(), CommonDateTimeUtils.formatDateToLog(worker.getHolidayCandidate()));

                worker.setSaveHolidayPhase(new SaveHolidayPhaseBuilder().build());

                return Uni.createFrom().item(Message.of(worker, message::ack));
            }
        }
    }
}