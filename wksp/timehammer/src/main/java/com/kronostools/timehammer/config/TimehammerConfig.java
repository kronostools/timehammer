package com.kronostools.timehammer.config;

import com.kronostools.timehammer.enums.Profile;
import com.kronostools.timehammer.service.TimeMachineService;
import io.quarkus.arc.config.ConfigProperties;
import io.quarkus.arc.config.ConfigProperties.NamingStrategy;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@ConfigProperties(prefix = "timehammer", namingStrategy= NamingStrategy.VERBATIM)
public class TimehammerConfig {
    private String baseUrl;
    private Set<String> admins;
    private Chatbot chatbot;
    private Timetable timetable;
    private Mocks mocks;
    private Schedules schedules;

    public static class Mocks {
        private Boolean comunytekClientMocked;
        private Boolean timeMachineServiceMocked;

        public Boolean isComunytekClientMocked() {
            return comunytekClientMocked;
        }

        public void setComunytekClientMocked(Boolean comunytekClientMocked) {
            this.comunytekClientMocked = comunytekClientMocked;
        }

        public Boolean isTimeMachineServiceMocked() {
            return timeMachineServiceMocked;
        }

        public void setTimeMachineServiceMocked(Boolean timeMachineServiceMocked) {
            this.timeMachineServiceMocked = timeMachineServiceMocked;
        }
    }

    public static class Timetable {
        private LocalTime min;
        private LocalTime max;

        public LocalTime getMin() {
            return min;
        }

        public void setMin(LocalTime min) {
            this.min = min;
        }

        public LocalTime getMax() {
            return max;
        }

        public void setMax(LocalTime max) {
            this.max = max;
        }
    }

    public static class Chatbot {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class ScheduledProcessConfig {
        private String name;
        private Boolean enabled;
        private Optional<Integer> intervalInMinutes;
        private String cron;
        private Optional<LocalTime> start;
        private Optional<LocalTime> end;

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

        public Optional<Integer> getIntervalInMinutes() {
            return intervalInMinutes;
        }

        public void setIntervalInMinutes(Optional<Integer> intervalInMinutes) {
            this.intervalInMinutes = intervalInMinutes;
        }

        public Long getIntervalInMillis() {
            return intervalInMinutes.map(minutes -> minutes * 60 * 1000L).orElse(60000L);
        }

        public String getCron() {
            return cron;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }

        public Optional<LocalTime> getStart() {
            return start;
        }

        public String getStartAsString(final String defaultValue) {
            return start.map(TimeMachineService::formatTimeSimple).orElse(defaultValue);
        }

        public void setStart(Optional<LocalTime> start) {
            this.start = start;
        }

        public Optional<LocalTime> getEnd() {
            return end;
        }

        public String getEndAsString(final String defaultValue) {
            return end.map(TimeMachineService::formatTimeSimple).orElse(defaultValue);
        }

        public void setEnd(Optional<LocalTime> end) {
            this.end = end;
        }

        public Boolean shouldBeRunAt(final LocalDateTime timestamp) {
            return start.map(s -> timestamp.toLocalTime().isAfter(s)).orElse(Boolean.TRUE)
                    && end.map(e -> timestamp.toLocalTime().isBefore(e)).orElse(Boolean.TRUE);
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

    public static enum ScheduleName {
        UPDATE_WORKERS_STATUS, UPDATE_WORKERS_HOLIDAYS, CLEAN_PAST_WORKERS_HOLIDAYS, CLEAN_SSID_TRACKING_EVENTS, CLEAN_TRASH_MESSAGES;
    }

    public static class Schedules {
        private ScheduledProcessConfig updateWorkersStatus;
        private ScheduledProcessConfig updateWorkersHolidays;
        private ScheduledProcessConfig cleanSsidTrackingEvents;
        private ScheduledProcessConfig cleanPastWorkersHolidays;
        private ScheduledProcessConfig cleanTrashMessages;

        public ScheduledProcessConfig getUpdateWorkersStatus() {
            return updateWorkersStatus;
        }

        public void setUpdateWorkersStatus(ScheduledProcessConfig updateWorkersStatus) {
            this.updateWorkersStatus = updateWorkersStatus;
        }

        public ScheduledProcessConfig getUpdateWorkersHolidays() {
            return updateWorkersHolidays;
        }

        public void setUpdateWorkersHolidays(ScheduledProcessConfig updateWorkersHolidays) {
            this.updateWorkersHolidays = updateWorkersHolidays;
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

        public ScheduledProcessConfig getByScheduleName(final ScheduleName scheduleName) {
            ScheduledProcessConfig result = null;

            switch (scheduleName) {
                case UPDATE_WORKERS_STATUS:
                    result = updateWorkersStatus;
                    break;
                case UPDATE_WORKERS_HOLIDAYS:
                    result = updateWorkersHolidays;
                    break;
                case CLEAN_SSID_TRACKING_EVENTS:
                    result = cleanSsidTrackingEvents;
                    break;
                case CLEAN_PAST_WORKERS_HOLIDAYS:
                    result = cleanPastWorkersHolidays;
                    break;
                case CLEAN_TRASH_MESSAGES:
                    result = cleanTrashMessages;
                    break;
            }

            return result;
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Set<String> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<String> admins) {
        this.admins = admins;
    }

    public Profile getProfile(final String externalId) {
        Profile result = Profile.WORKER;

        if (admins.contains(externalId)) {
            result = Profile.ADMIN;
        }

        return result;
    }

    public Chatbot getChatbot() {
        return chatbot;
    }

    public void setChatbot(Chatbot chatbot) {
        this.chatbot = chatbot;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public Mocks getMocks() {
        return mocks;
    }

    public void setMocks(Mocks mocks) {
        this.mocks = mocks;
    }

    public Schedules getSchedules() {
        return schedules;
    }

    public void setSchedules(Schedules schedules) {
        this.schedules = schedules;
    }

    public String getRegistrationUrl(final String internalId) {
        return baseUrl + "/auth/register?internalId=" + internalId;
    }

    public String getUpdatePasswordUrl(final String internalId) {
        return baseUrl + "/auth/updatePassword?internalId=" + internalId;
    }

    public String getHelpUrl() {
        return baseUrl + "/faq.html";
    }
}