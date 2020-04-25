package com.kronostools.timehammer.comunytek.dto;

import com.kronostools.timehammer.comunytek.enums.ComunytekStatusValue;
import com.kronostools.timehammer.enums.SupportedTimezone;
import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.kronostools.timehammer.utils.Constants.LINE_BREAK;
import static com.kronostools.timehammer.utils.Constants.TAB;

public class ComunytekStatusDto {
    private final String username;
    private final LocalDateTime timestamp;
    private final ComunytekStatusValue status;

    ComunytekStatusDto(final String username, final LocalDateTime timestamp, final ComunytekStatusValue status) {
        this.username = username;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ComunytekStatusValue getStatus() {
        return status;
    }

    public static ComunytekStatusDto fromResponse(final String username, final String response, final LocalDateTime timestamp, final SupportedTimezone timezone) {
        ComunytekStatusDto result;

        if (response != null && response.length() > 0) {
            final String[] hoursReportedParts = response.split(LINE_BREAK);

            result = Stream.of(hoursReportedParts)
                    .skip(hoursReportedParts.length - 1)
                    .findFirst()
                    .map(hourReport -> hourReport.split(TAB))
                    .map(hourReportParts -> {
                        final String[] dateTimeParts = new String[] { hourReportParts[0], hourReportParts[1] };
                        final LocalDateTime statusDateTime = TimeMachineService.parseDateTimeWithZone(dateTimeParts, new String[] {TimeMachineService.FORMAT_DDMMYYYY_SEP_FWS, TimeMachineService.FORMAT_HHMMSSSSS}, timezone);

                        return new ComunytekStatusDto(username, statusDateTime, ComunytekStatusValue.fromCode(hourReportParts[2]));
                    })
                    .get();
        } else {
            result = new ComunytekStatusDto(username, TimeMachineService.getDateTimeWithZoneAtStartOfDay(timestamp, timezone), ComunytekStatusValue.INITIAL);
        }

        return result;
    }

    @Override
    public String toString() {
        return "ComunytekStatusDto{" +
                "username='" + username + '\'' +
                ", day=" + TimeMachineService.formatDateTimeFull(timestamp) +
                ", status=" + status +
                '}';
    }
}