package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class WorkerNonWorkingDaysVo {
    private final String workerExternalId;
    private final Set<LocalDate> workerHolidays;
    private final Set<LocalDate> cityHolidays;

    public WorkerNonWorkingDaysVo(final String workerExternalId, final Set<LocalDate> workerHolidays, final Set<LocalDate> cityHolidays) {
        this.workerExternalId = workerExternalId;
        this.workerHolidays = workerHolidays;
        this.cityHolidays = cityHolidays;
    }

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public Set<LocalDate> getWorkerHolidays() {
        return workerHolidays;
    }

    public Set<LocalDate> getCityHolidays() {
        return cityHolidays;
    }

    @Override
    public String toString() {
        return "WorkerNonWorkingDaysVo{" +
                "workerExternalId='" + workerExternalId + '\'' +
                ", holidays=" + workerHolidays.stream().map(TimeMachineService::formatDate).collect(Collectors.joining(",", "[", "]")) +
                ", nonWorkingDays=" + cityHolidays.stream().map(TimeMachineService::formatDate).collect(Collectors.joining(",", "[", "]")) +
                '}';
    }
}