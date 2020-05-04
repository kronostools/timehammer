package com.kronostools.timehammer.scheduler.schedules;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
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
public class TestSchedule extends Schedule<Integer> {

    private final Emitter<Integer> scheduleChannel;

    public TestSchedule(final SchedulesConfig schedulesConfig,
                        final TimeMachineService timeMachineService,
                        @Channel(Channels.HOLIDAYS_UPDATE) final Emitter<Integer> scheduleChannel) {
        super(schedulesConfig, timeMachineService);
        this.scheduleChannel = scheduleChannel;
    }

    @Scheduled(cron = "{timehammer.schedules.update-workers-holidays.cron}")
    void test() {
        run();
    }

    @Override
    protected Emitter<Integer> getChannel() {
        return scheduleChannel;
    }

    @Override
    protected Integer getTriggerMessage(final LocalDateTime timestamp) {
        LOG.debug("TES Timestamp: {}", CommonDateTimeUtils.formatDateTimeToLog(timeMachineService.getNow()));
        return 1;
    }

    @Override
    protected ScheduledProcessConfig getConfig() {
        return schedulesConfig.getUpdateWorkersHolidays();
    }
}