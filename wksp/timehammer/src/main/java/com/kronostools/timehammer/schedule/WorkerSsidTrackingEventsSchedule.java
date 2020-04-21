package com.kronostools.timehammer.schedule;

import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.config.TimehammerConfig.ScheduleName;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.service.WorkerSsidTrackingEventService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class WorkerSsidTrackingEventsSchedule extends Schedule {

    private final WorkerSsidTrackingEventService workerSsidTrackingEventService;

    public WorkerSsidTrackingEventsSchedule(final TimehammerConfig timehammerConfig,
                                            final TimeMachineService timeMachineService,
                                            final WorkerSsidTrackingEventService workerSsidTrackingEventService) {
        super(timehammerConfig, timeMachineService);
        this.workerSsidTrackingEventService = workerSsidTrackingEventService;
    }

    @Scheduled(cron = "{timehammer.schedules.cleanSsidTrackingEvents.cron}")
    void cleanSsidTrackingEvents() {
        run();
    }

    @Override
    protected void mainLogic(LocalDateTime timestamp) {
        workerSsidTrackingEventService.cleanSsidTrackingEventsUntil(timestamp);
    }

    @Override
    protected ScheduleName getScheduleName() {
        return ScheduleName.CLEAN_SSID_TRACKING_EVENTS;
    }
}