package com.kronostools.timehammer.schedule;

import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.config.TimehammerConfig.ScheduleName;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.service.WorkerService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class CleanPastWorkersHolidaysSchedule extends Schedule {
    private final WorkerService workerService;

    public CleanPastWorkersHolidaysSchedule(final TimehammerConfig timehammerConfig,
                                            final TimeMachineService timeMachineService,
                                            final WorkerService workerService) {
        super(timehammerConfig, timeMachineService);
        this.workerService = workerService;
    }

    @Scheduled(cron = "{timehammer.schedules.cleanPastWorkersHolidays.cron}")
    void cleanPastWorkersHolidays() {
        run();
    }

    @Override
    protected void mainLogic(LocalDateTime timestamp) {
        workerService.cleanPastWorkersHolidaysUntil(timestamp);
    }

    @Override
    protected ScheduleName getScheduleName() {
        return ScheduleName.CLEAN_PAST_WORKERS_HOLIDAYS;
    }
}