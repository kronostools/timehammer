package com.kronostools.timehammer.schedule;

import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.config.TimehammerConfig.ScheduleName;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.service.WorkerService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class UpdateWorkersHolidaysSchedule extends Schedule {
    private final WorkerService workerService;

    public UpdateWorkersHolidaysSchedule(final TimehammerConfig timehammerConfig,
                                         final TimeMachineService timeMachineService,
                                         final WorkerService workerService) {
        super(timehammerConfig, timeMachineService);
        this.workerService = workerService;
    }

    @Scheduled(cron = "{timehammer.schedules.updateWorkersHolidays.cron}")
    void updateWorkersHolidays() {
        run();
    }

    @Override
    protected void mainLogic(LocalDateTime timestamp) {
        workerService.updateWorkersHolidays();
    }

    @Override
    protected ScheduleName getScheduleName() {
        return ScheduleName.UPDATE_WORKERS_HOLIDAYS;
    }
}