package com.kronostools.timehammer.scheduler.schedules;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessage;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.scheduler.config.SchedulesConfig;
import com.kronostools.timehammer.scheduler.config.SchedulesConfig.ScheduledProcessConfig;
import com.kronostools.timehammer.scheduler.services.TimeMachineService;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class UpdateWorkersHolidaysSchedule extends Schedule<ScheduleTriggerMessage> {

    private final Emitter<ScheduleTriggerMessage> scheduleChannel;

    public UpdateWorkersHolidaysSchedule(final SchedulesConfig schedulesConfig,
                                         final TimeMachineService timeMachineService,
                                         @Channel(Channels.HOLIDAYS_UPDATE) final Emitter<ScheduleTriggerMessage> scheduleChannel) {
        super(schedulesConfig, timeMachineService);
        this.scheduleChannel = scheduleChannel;
    }

    @Scheduled(cron = "{timehammer.schedules.update-workers-holidays.cron}")
    void updateWorkersHolidays() {
        run();
    }

    @Override
    protected Emitter<ScheduleTriggerMessage> getChannel() {
        return scheduleChannel;
    }

    @Override
    protected ScheduleTriggerMessage getTriggerMessage(final LocalDateTime timestamp) {
        LOG.debug("HOL Timestamp: {}", CommonDateTimeUtils.formatDateTimeToLog(timeMachineService.getNow()));
        return ScheduleTriggerMessage.Builder.builder()
                .timestamp(timestamp)
                .name(getConfig().getName())
                .build();
    }

    @Override
    protected ScheduledProcessConfig getConfig() {
        return schedulesConfig.getUpdateWorkersHolidays();
    }
}