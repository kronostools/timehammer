package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class WorkerNonWorkingDaysVo {
    private final String workerInternalId;
    private final Set<LocalDate> workerHolidays;
    private final Set<LocalDate> cityHolidays;

    public WorkerNonWorkingDaysVo(final String workerInternalId, final Set<LocalDate> workerHolidays, final Set<LocalDate> cityHolidays) {
        this.workerInternalId = workerInternalId;
        this.workerHolidays = workerHolidays;
        this.cityHolidays = cityHolidays;
    }

    public String getWorkerInternalId() {
        return workerInternalId;
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
                "workerInternalId='" + workerInternalId + '\'' +
                ", holidays=" + workerHolidays.stream().map(TimeMachineService::formatDate).collect(Collectors.joining(",", "[", "]")) +
                ", nonWorkingDays=" + cityHolidays.stream().map(TimeMachineService::formatDate).collect(Collectors.joining(",", "[", "]")) +
                '}';
    }
}