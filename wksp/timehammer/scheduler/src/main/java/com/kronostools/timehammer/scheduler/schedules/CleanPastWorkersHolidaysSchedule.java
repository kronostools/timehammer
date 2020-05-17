package com.kronostools.timehammer.scheduler.schedules;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessage;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.scheduler.config.SchedulesConfig;
import com.kronostools.timehammer.scheduler.config.SchedulesConfig.ScheduledProcessConfig;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CleanPastWorkersHolidaysSchedule extends Schedule {

    private final Emitter<ScheduleTriggerMessage> scheduleChannel;

    public CleanPastWorkersHolidaysSchedule(final SchedulesConfig schedulesConfig,
                                            final TimeMachineService timeMachineService,
                                            @Channel(Channels.SCHEDULE_CLEAN_HOLIDAYS) final Emitter<ScheduleTriggerMessage> scheduleChannel) {
        super(schedulesConfig, timeMachineService);
        this.scheduleChannel = scheduleChannel;
    }

    @Scheduled(cron = "{timehammer.schedules.clean-past-workers-holidays.cron}")
    void cleanPastWorkersHolidays() {
        run();
    }

    @Override
    protected Emitter<ScheduleTriggerMessage> getChannel() {
        return scheduleChannel;
    }

    @Override
    protected ScheduledProcessConfig getConfig() {
        return schedulesConfig.getUpdateWorkersHoliday();
    }
}