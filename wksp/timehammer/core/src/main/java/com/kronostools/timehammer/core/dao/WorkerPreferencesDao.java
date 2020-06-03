package com.kronostools.timehammer.core.dao;

import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.registration.forms.Timetable;
import com.kronostools.timehammer.core.model.InsertResult;
import com.kronostools.timehammer.core.model.InsertResultBuilder;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class WorkerPreferencesDao {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerPreferencesDao.class);

    private final PgPool client;

    public WorkerPreferencesDao(final PgPool client) {
        this.client = client;
    }

    public Uni<InsertResult> insertWorkerPreferences(final String workerInternalId, final String workerExternalId, final Timetable defaultTimetable, final Company company, final String workSsid, final String workCityCode) {
        final List<Object> params = new ArrayList<>() {{
            add(workerInternalId);
            add(workerExternalId);
            add(defaultTimetable.getWorkStartMon());
            add(defaultTimetable.getWorkEndMon());
            add(defaultTimetable.getLunchStartMon());
            add(defaultTimetable.getLunchEndMon());
            add(defaultTimetable.getWorkStartMon());
            add(defaultTimetable.getWorkEndMon());
            add(defaultTimetable.getLunchStartMon());
            add(defaultTimetable.getLunchEndMon());
            add(defaultTimetable.getWorkStartMon());
            add(defaultTimetable.getWorkEndMon());
            add(defaultTimetable.getLunchStartMon());
            add(defaultTimetable.getLunchEndMon());
            add(defaultTimetable.getWorkStartMon());
            add(defaultTimetable.getWorkEndMon());
            add(defaultTimetable.getLunchStartMon());
            add(defaultTimetable.getLunchEndMon());
            add(defaultTimetable.getWorkStartMon());
            add(defaultTimetable.getWorkEndMon());
            add(defaultTimetable.getLunchStartMon());
            add(defaultTimetable.getLunchEndMon());
            add(company);
            add(workSsid);
            add(workCityCode);
        }};

        return client
                .preparedQuery(
                        "INSERT INTO worker_preferences(" +
                            "    worker_internal_id, worker_external_id, " +
                            "    work_start_mon, work_end_mon, lunch_start_mon, lunch_end_mon, " +
                            "    work_start_tue, work_end_tue, lunch_start_tue, lunch_end_tue, " +
                            "    work_start_wed, work_end_wed, lunch_start_wed, lunch_end_wed, " +
                            "    work_start_thu, work_end_thu, lunch_start_thu, lunch_end_thu, " +
                            "    work_start_fri, work_end_fri, lunch_start_fri, lunch_end_fri, " +
                            "    company_code, work_ssid, work_city_code" +
                            ") " +
                            "VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18, $19, $20, $21, $22, $23, $24, $25)",
                            Tuple.of(params))
                .map((pgRowSet) -> new InsertResultBuilder()
                        .inserted(pgRowSet.rowCount())
                        .build());
    }
}
