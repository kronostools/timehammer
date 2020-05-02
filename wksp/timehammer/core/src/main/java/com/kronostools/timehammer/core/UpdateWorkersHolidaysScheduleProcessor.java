package com.kronostools.timehammer.core;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessage;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidays;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidaysWorker;
import com.kronostools.timehammer.core.dao.WorkerCurrentPreferencesDao;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UpdateWorkersHolidaysScheduleProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateWorkersHolidaysScheduleProcessor.class);

    private final WorkerCurrentPreferencesDao workerCurrentPreferencesDao;

    public UpdateWorkersHolidaysScheduleProcessor(final WorkerCurrentPreferencesDao workerCurrentPreferencesDao) {
        this.workerCurrentPreferencesDao = workerCurrentPreferencesDao;
    }

    @Incoming(Channels.HOLIDAYS_UPDATE)
    @Outgoing(Channels.HOLIDAYS_WORKER_AUTH)
    public Uni<UpdateWorkersHolidays> process(final ScheduleTriggerMessage updateWorkersHolidaysTriggerMessage) {
        LOG.info("Received trigger message to run schedule '{}'", updateWorkersHolidaysTriggerMessage.getName());

        return workerCurrentPreferencesDao.findAll(updateWorkersHolidaysTriggerMessage.getTimestamp())
                .flatMap(wcpl -> {
                    List<UpdateWorkersHolidaysWorker> workers = wcpl.stream()
                        .map(wcp -> new UpdateWorkersHolidaysWorker(wcp.getWorkerInternalId(), wcp.getWorkerExternalId(), wcp.canBeNotified(updateWorkersHolidaysTriggerMessage.getTimestamp().toLocalTime())))
                        .collect(Collectors.toList());

                    return Uni.createFrom().item(new UpdateWorkersHolidays(updateWorkersHolidaysTriggerMessage.getTimestamp(), workers));
                });
    }
}