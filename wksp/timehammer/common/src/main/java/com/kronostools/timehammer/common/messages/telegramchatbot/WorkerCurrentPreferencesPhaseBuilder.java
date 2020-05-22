package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.WorkerCurrentPreferencesResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@JsonPOJOBuilder(withPrefix = "")
public class WorkerCurrentPreferencesPhaseBuilder extends PhaseBuilder<WorkerCurrentPreferencesResult, WorkerCurrentPreferencesPhaseBuilder> {
    private LocalDate date;
    private String workerInternalId;
    private String workerExternalId;
    private String workSsid;
    private LocalTime workStart;
    private LocalTime workEnd;
    private LocalTime lunchStart;
    private LocalTime lunchEnd;
    private String workCityCode;
    private SupportedTimezone timezone;
    private Company company;
    private boolean workerHoliday;
    private boolean cityHoliday;
    private Set<String> chatIds;

    public static WorkerCurrentPreferencesPhase copyAndBuild(final WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase) {
        return Optional.ofNullable(workerCurrentPreferencesPhase)
                .map(wcpp -> WorkerCurrentPreferencesPhaseBuilder.copy(wcpp).build())
                .orElse(null);
    }

    public static WorkerCurrentPreferencesPhaseBuilder copy(final WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase) {
        return Optional.ofNullable(workerCurrentPreferencesPhase)
                .map(wcpp -> new WorkerCurrentPreferencesPhaseBuilder()
                        .result(wcpp.getResult())
                        .errorMessage(wcpp.getErrorMessage())
                        .date(wcpp.getDate())
                        .workerInternalId(wcpp.getWorkerInternalId())
                        .workerExternalId(wcpp.getWorkerExternalId())
                        .workSsid(wcpp.getWorkSsid())
                        .workStart(wcpp.getWorkStart())
                        .workEnd(wcpp.getWorkEnd())
                        .lunchStart(wcpp.getLunchStart())
                        .lunchEnd(wcpp.getLunchEnd())
                        .workCityCode(wcpp.getWorkCityCode())
                        .timezone(wcpp.getTimezone())
                        .company(wcpp.getCompany())
                        .workerHoliday(wcpp.isWorkerHoliday())
                        .cityHoliday(wcpp.isCityHoliday())
                        .chatIds(wcpp.getChatIds()))
                .orElse(null);
    }

    public WorkerCurrentPreferencesPhaseBuilder date(final LocalDate date) {
        this.date = date;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder workerInternalId(final String workerInternalId) {
        this.workerInternalId = workerInternalId;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder workerExternalId(final String workerExternalId) {
        this.workerExternalId = workerExternalId;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder workSsid(final String workSsid) {
        this.workSsid = workSsid;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder workStart(final LocalTime workStart) {
        this.workStart = workStart;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder workEnd(final LocalTime workEnd) {
        this.workEnd = workEnd;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder lunchStart(final LocalTime lunchStart) {
        this.lunchStart = lunchStart;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder lunchEnd(final LocalTime lunchEnd) {
        this.lunchEnd = lunchEnd;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder workCityCode(final String workCityCode) {
        this.workCityCode = workCityCode;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder timezone(final SupportedTimezone timezone) {
        this.timezone = timezone;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder company(final Company company) {
        this.company = company;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder workerHoliday(final boolean workerHoliday) {
        this.workerHoliday = workerHoliday;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder cityHoliday(final boolean cityHoliday) {
        this.cityHoliday = cityHoliday;
        return this;
    }

    public WorkerCurrentPreferencesPhaseBuilder chatIds(final Set<String> chatIds) {
        this.chatIds = chatIds;
        return this;
    }

    public WorkerCurrentPreferencesPhase build() {
        final WorkerCurrentPreferencesPhase wcpp = new WorkerCurrentPreferencesPhase(result, errorMessage);
        wcpp.setDate(date);
        wcpp.setWorkerInternalId(workerInternalId);
        wcpp.setWorkerExternalId(workerExternalId);
        wcpp.setWorkSsid(workSsid);
        wcpp.setWorkStart(workStart);
        wcpp.setWorkEnd(workEnd);
        wcpp.setLunchStart(lunchStart);
        wcpp.setLunchEnd(lunchEnd);
        wcpp.setWorkCityCode(workCityCode);
        wcpp.setTimezone(timezone);
        wcpp.setCompany(company);
        wcpp.setWorkerHoliday(workerHoliday);
        wcpp.setCityHoliday(cityHoliday);
        wcpp.setChatIds(chatIds);

        return wcpp;
    }
}