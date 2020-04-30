package com.kronostools.timehammer.schedule;

import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.service.TimeMachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public abstract class Schedule {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected TimehammerConfig timehammerConfig;
    protected TimeMachineService timeMachineService;

    public Schedule() {} // dummy constructor

    public Schedule(final TimehammerConfig timehammerConfig,
                    final TimeMachineService timeMachineService) {
        this.timehammerConfig = timehammerConfig;
        this.timeMachineService = timeMachineService;
    }

    protected void run() {
        final LocalDateTime timestamp = timeMachineService.getNow();

        if (getConfig().isEnabled()) {
            if (getConfig().shouldBeRunAt(timestamp)) {
                LOG.info("BEGIN [schedule] {}", getName());

                mainLogic(timestamp);

                LOG.info("END [schedule] {}", getName());
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("[schedule] {} only executes from {} to {} at UTC",
                            getName(),
                            getConfig().getStartAsString("N/A"),
                            getConfig().getEndAsString("N/A"));
                }
            }
        } else {
            LOG.debug("[schedule] {} is disabled", getName());
        }
    }

    protected abstract void mainLogic(final LocalDateTime timestamp);

    protected abstract TimehammerConfig.ScheduleName getScheduleName();

    protected TimehammerConfig.ScheduledProcessConfig getConfig() {
        return timehammerConfig.getSchedules().getByScheduleName(getScheduleName());
    }

    protected String getName() {
        return getConfig().getName();
    }
}