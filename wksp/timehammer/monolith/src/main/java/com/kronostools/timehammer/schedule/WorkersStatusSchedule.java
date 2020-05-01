package com.kronostools.timehammer.schedule;

import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.service.WorkerService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class WorkersStatusSchedule extends Schedule {
    private final WorkerService workerService;

    public WorkersStatusSchedule(final TimehammerConfig timehammerConfig,
                                 final TimeMachineService timeMachineService,
                                 final WorkerService workerService) {
        super(timehammerConfig, timeMachineService);
        this.workerService = workerService;
    }

    @Scheduled(cron = "{timehammer.schedules.updateWorkersStatus.cron}")
    void updateWorkersStatus() {
        run();
    }

    @Override
    protected void mainLogic(final LocalDateTime timestamp) {
        workerService.updateWorkersStatus(timestamp);
    }

    @Override
    protected TimehammerConfig.ScheduleName getScheduleName() {
        return TimehammerConfig.ScheduleName.UPDATE_WORKERS_STATUS;
    }

}