package com.kronostools.timehammer.comunytek.dto;

import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.kronostools.timehammer.utils.Constants.LINE_BREAK;
import static com.kronostools.timehammer.utils.Constants.TAB;

public class ComunytekHolidaysDto {
    private final String username;
    private final Set<LocalDate> holidays;

    ComunytekHolidaysDto(final String username, final Set<LocalDate> holidays) {
        this.username = username;
        this.holidays = holidays;
    }

    public String getUsername() {
        return username;
    }

    public Set<LocalDate> getHolidays() {
        return holidays;
    }

    public static ComunytekHolidaysDto fromResponse(final String username, final String response) {
        final Set<LocalDate> holidays = Stream.of(response.split(LINE_BREAK))
                .map(rawHoliday -> rawHoliday.split(TAB))
                .map(holidayParts -> TimeMachineService.parseDate(holidayParts[0], TimeMachineService.FORMAT_DDMMYYYY_SEP_FWS))
                .collect(Collectors.toSet());

        return new ComunytekHolidaysDto(username, holidays);
    }

    @Override
    public String toString() {
        return "ComunytekHolidaysDto{" +
                "username='" + username + '\'' +
                ", holidays=[" + holidays.stream().map(TimeMachineService::formatDate).collect(Collectors.joining("', '", "'", "'")) +
                "]}";
    }
}