package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;
import com.kronostools.timehammer.common.messages.schedules.*;
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
public class CleanPastWorkersHolidaysScheduleProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(CleanPastWorkersHolidaysScheduleProcessor.class);

    private final WorkerHolidaysDao workerHolidaysDao;

    public CleanPastWorkersHolidaysScheduleProcessor(final WorkerHolidaysDao workerHolidaysDao) {
        this.workerHolidaysDao = workerHolidaysDao;
    }

    @Incoming(Channels.SCHEDULE_CLEAN_HOLIDAYS)
    @Outgoing(Channels.HOLIDAYS_CLEAN_SUMMARY)
    public Uni<Message<CleanPastWorkersHolidays>> process(final Message<ScheduleTriggerMessage> message) {
        final ScheduleTriggerMessage triggerMessage = message.getPayload();

        LOG.info("Received trigger message to run schedule '{}' with timestamp '{}'", triggerMessage.getName(), CommonDateTimeUtils.formatDateTimeToLog(triggerMessage.getGenerated()));

        return workerHolidaysDao.cleanPastHolidays(triggerMessage.getGenerated().toLocalDate())
                .onItem()
                .produceUni((deleteResult) -> {
                    final CleanPastHolidaysPhase dbResult;

                    if (deleteResult.isSuccessful()) {
                        dbResult = new CleanPastHolidaysPhaseBuilder()
                                .result(SimpleResult.OK)
                                .build();
                    } else {
                        dbResult = new CleanPastHolidaysPhaseBuilder()
                                .result(SimpleResult.KO)
                                .errorMessage(deleteResult.getErrorMessage())
                                .build();
                    }

                    final CleanPastWorkersHolidays scheduleResult = new CleanPastWorkersHolidaysBuilder()
                            .generated(triggerMessage.getGenerated())
                            .executionId(triggerMessage.getExecutionId())
                            .name(triggerMessage.getName())
                            .cleanPastHolidaysPhase(dbResult)
                            .build();

                    return Uni.createFrom().item(Message.of(scheduleResult, message::ack));
                });
    }
}