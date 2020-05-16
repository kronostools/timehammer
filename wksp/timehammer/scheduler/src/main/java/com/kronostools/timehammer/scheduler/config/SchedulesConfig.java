package com.kronostools.timehammer.scheduler.config;

import io.quarkus.arc.config.ConfigProperties;
import io.quarkus.arc.config.ConfigProperties.NamingStrategy;

import java.util.Objects;

@ConfigProperties(prefix = "timehammer.schedules", namingStrategy= NamingStrategy.KEBAB_CASE)
public class SchedulesConfig {
    private ScheduledProcessConfig updateWorkersStatus;
    private ScheduledProcessConfig updateWorkersHoliday;
    private ScheduledProcessConfig cleanSsidTrackingEvents;
    private ScheduledProcessConfig cleanPastWorkersHolidays;
    private ScheduledProcessConfig cleanTrashMessages;

    public static class ScheduledProcessConfig {
        private String name;
        private Boolean enabled;
        private String cron;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getCron() {
            return cron;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ScheduledProcessConfig that = (ScheduledProcessConfig) o;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    public ScheduledProcessConfig getUpdateWorkersStatus() {
        return updateWorkersStatus;
    }

    public void setUpdateWorkersStatus(ScheduledProcessConfig updateWorkersStatus) {
        this.updateWorkersStatus = updateWorkersStatus;
    }

    public ScheduledProcessConfig getUpdateWorkersHoliday() {
        return updateWorkersHoliday;
    }

    public void setUpdateWorkersHoliday(ScheduledProcessConfig updateWorkersHoliday) {
        this.updateWorkersHoliday = updateWorkersHoliday;
    }

    public ScheduledProcessConfig getCleanSsidTrackingEvents() {
        return cleanSsidTrackingEvents;
    }

    public void setCleanSsidTrackingEvents(ScheduledProcessConfig cleanSsidTrackingEvents) {
        this.cleanSsidTrackingEvents = cleanSsidTrackingEvents;
    }

    public ScheduledProcessConfig getCleanPastWorkersHolidays() {
        return cleanPastWorkersHolidays;
    }

    public void setCleanPastWorkersHolidays(ScheduledProcessConfig cleanPastWorkersHolidays) {
        this.cleanPastWorkersHolidays = cleanPastWorkersHolidays;
    }

    public ScheduledProcessConfig getCleanTrashMessages() {
        return cleanTrashMessages;
    }

    public void setCleanTrashMessages(ScheduledProcessConfig cleanTrashMessages) {
        this.cleanTrashMessages = cleanTrashMessages;
    }
}