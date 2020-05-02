package com.kronostools.timehammer.scheduler.schedules;

import com.kronostools.timehammer.scheduler.config.SchedulesConfig;
import com.kronostools.timehammer.scheduler.config.SchedulesConfig.ScheduledProcessConfig;
import com.kronostools.timehammer.scheduler.services.TimeMachineService;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public abstract class Schedule<T> {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected SchedulesConfig schedulesConfig;
    protected TimeMachineService timeMachineService;

    public Schedule() {} // dummy constructor

    public Schedule(final SchedulesConfig schedulesConfig,
                    final TimeMachineService timeMachineService) {
        this.schedulesConfig = schedulesConfig;
        this.timeMachineService = timeMachineService;
    }

    protected void run() {
        final LocalDateTime timestamp = timeMachineService.getNow();

        final ScheduledProcessConfig config = getConfig();

        if (config.isEnabled()) {
            LOG.debug("[schedule] {} - emitting message to trigger schedule ...", config.getName());

            getChannel().send(getTriggerMessage(timestamp)).handle((Void, e) -> {
                if (e != null) {
                    LOG.warn("[schedule] {} - error emitting trigger message", config.getName());
                } else {
                    LOG.info("[schedule] {} - trigger message was emitted successfully", config.getName());
                }

                return null;
            });
        } else {
            LOG.debug("[schedule] {} is disabled", config.getName());
        }
    }

    protected abstract Emitter<T> getChannel();

    protected abstract T getTriggerMessage(final LocalDateTime timestamp);

    protected abstract ScheduledProcessConfig getConfig();
}