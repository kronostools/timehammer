package com.kronostools.timehammer.core.dao;

import com.kronostools.timehammer.core.model.WorkerCurrentPreferences;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class WorkerCurrentPreferencesDao {

    private final PgPool client;

    public WorkerCurrentPreferencesDao(final PgPool client) {
        this.client = client;
    }

    public Uni<List<WorkerCurrentPreferences>> findAll(final LocalDateTime timestamp) {
        final String dayOfWeek = timestamp.toLocalDate().getDayOfWeek().name();

        return client.preparedQuery(
                "SELECT p.worker_internal_id as worker_internal_id, p.worker_external_id as worker_external_id, p.work_ssid as work_ssid, " +
                    "CASE WHEN $1 = 'MONDAY' THEN p.work_start_mon ELSE (CASE WHEN $1 = 'TUESDAY' THEN p.work_start_tue ELSE (CASE WHEN $1 = 'WEDNESDAY' THEN p.work_start_wed ELSE (CASE WHEN $1 = 'THURSDAY' THEN p.work_start_thu ELSE (CASE WHEN $1 = 'FRIDAY' THEN p.work_start_fri ELSE NULL END) END) END) END) END as work_start, " +
                    "CASE WHEN $1 = 'MONDAY' THEN p.work_end_mon ELSE (CASE WHEN $1 = 'TUESDAY' THEN p.work_end_tue ELSE (CASE WHEN $1 = 'WEDNESDAY' THEN p.work_end_wed ELSE (CASE WHEN $1 = 'THURSDAY' THEN p.work_end_thu ELSE (CASE WHEN $1 = 'FRIDAY' THEN p.work_end_fri ELSE NULL END) END) END) END) END as work_end, " +
                    "CASE WHEN $1 = 'MONDAY' THEN p.lunch_start_mon ELSE (CASE WHEN $1 = 'TUESDAY' THEN p.lunch_start_tue ELSE (CASE WHEN $1 = 'WEDNESDAY' THEN p.lunch_start_wed ELSE (CASE WHEN $1 = 'THURSDAY' THEN p.lunch_start_thu ELSE (CASE WHEN $1 = 'FRIDAY' THEN p.lunch_start_fri ELSE NULL END) END) END) END) END as lunch_start, " +
                    "CASE WHEN $1 = 'MONDAY' THEN p.lunch_end_mon ELSE (CASE WHEN $1 = 'TUESDAY' THEN p.lunch_end_tue ELSE (CASE WHEN $1 = 'WEDNESDAY' THEN p.lunch_end_wed ELSE (CASE WHEN $1 = 'THURSDAY' THEN p.lunch_end_thu ELSE (CASE WHEN $1 = 'FRIDAY' THEN p.lunch_end_fri ELSE NULL END) END) END) END) END as lunch_end, " +
                    "p.work_city_code as work_city_code, c.timezone as timezone, p.company_code as company_code, " +
                    "wh.day is not null as worker_holiday, " +
                    "ch.day is not null as city_holiday " +
                    "FROM worker_preferences p " +
                    "INNER JOIN city c ON c.code = p.work_city_code " +
                    "LEFT OUTER JOIN city_holiday ch ON ch.city_code = p.work_city_code AND ch.day = $2 " +
                    "LEFT OUTER JOIN worker_holiday wh ON wh.worker_internal_id = p.worker_internal_id AND wh.day = $2",
                    Tuple.of(dayOfWeek, timestamp.toLocalDate())
                )
                .map(pgRowSet -> {
                    List<WorkerCurrentPreferences> list = new ArrayList<>(pgRowSet.size());

                    pgRowSet
                            .forEach(row -> list.add(WorkerCurrentPreferences.from(timestamp.toLocalDate(), row)));

                    return list;
                });
    }
}