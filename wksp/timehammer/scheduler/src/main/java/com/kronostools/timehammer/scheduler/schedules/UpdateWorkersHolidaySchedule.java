package com.kronostools.timehammer.scheduler.schedules;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessage;
import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessageBuilder;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.scheduler.config.SchedulesConfig;
import com.kronostools.timehammer.scheduler.config.SchedulesConfig.ScheduledProcessConfig;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class UpdateWorkersHolidaySchedule extends Schedule<ScheduleTriggerMessage> {

    private final Emitter<ScheduleTriggerMessage> scheduleChannel;

    public UpdateWorkersHolidaySchedule(final SchedulesConfig schedulesConfig,
                                        final TimeMachineService timeMachineService,
                                        @Channel(Channels.SCHEDULE_UPDATE_HOLIDAYS) final Emitter<ScheduleTriggerMessage> scheduleChannel) {
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
        return new ScheduleTriggerMessageBuilder()
                .timestamp(timestamp)
                .name(getConfig().getName())
                .build();
    }

    @Override
    protected ScheduledProcessConfig getConfig() {
        return schedulesConfig.getUpdateWorkersHolidays();
    }
}