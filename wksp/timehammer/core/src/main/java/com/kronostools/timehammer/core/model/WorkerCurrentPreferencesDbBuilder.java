package com.kronostools.timehammer.core.model;

import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.messages.schedules.model.WorkerCurrentPreferencesBuilder;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import io.vertx.mutiny.sqlclient.Row;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class WorkerCurrentPreferencesDbBuilder {

    public static WorkerCurrentPreferencesBuilder from(final LocalDate date, final Row row) {
        final String workerInternalId = row.getString("worker_internal_id");
        final String workerExternalId = row.getString("worker_external_id");
        final String workSsid = row.getString("work_ssid");

        final SupportedTimezone timezone = SupportedTimezone.fromTimezoneName(row.getString("timezone"));

        final ZoneOffset zoneOffSet = timezone.getOffset(date);

        final LocalTime workStart = CommonDateTimeUtils.getTimeWithOffset(row.getLocalTime("work_start"), zoneOffSet);
        final LocalTime workEnd = CommonDateTimeUtils.getTimeWithOffset(row.getLocalTime("work_end"), zoneOffSet);
        final LocalTime lunchStart = CommonDateTimeUtils.getTimeWithOffset(row.getLocalTime("lunch_start"), zoneOffSet);
        final LocalTime lunchEnd = CommonDateTimeUtils.getTimeWithOffset(row.getLocalTime("lunch_end"), zoneOffSet);
        final String cityCode = row.getString("work_city_code");
        final Company company = Company.fromCode(row.getString("company_code"));
        final Boolean workerHoliday = row.getBoolean("worker_holiday");
        final Boolean cityHoliday = row.getBoolean("city_holiday");
        final String chatId = row.getString("chat_id");

        return new WorkerCurrentPreferencesBuilder()
            .date(date)
            .workerInternalId(workerInternalId)
            .workerExternalId(workerExternalId)
            .workSsid(workSsid)
            .workStart(workStart)
            .workEnd(workEnd)
            .lunchStart(lunchStart)
            .lunchEnd(lunchEnd)
            .workCityCode(cityCode)
            .timezone(timezone)
            .company(company)
            .workerHoliday(workerHoliday)
            .cityHoliday(cityHoliday)
            .addChatId(chatId);
    }
}