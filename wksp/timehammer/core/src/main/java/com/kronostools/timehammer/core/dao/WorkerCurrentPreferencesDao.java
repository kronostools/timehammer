package com.kronostools.timehammer.core.dao;

import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.common.utils.CommonUtils;
import com.kronostools.timehammer.core.model.*;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WorkerCurrentPreferencesDao {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerCurrentPreferencesDao.class);

    private final PgPool client;

    public WorkerCurrentPreferencesDao(final PgPool client) {
        this.client = client;
    }

    public Uni<WorkerCurrentPreferencesMultipleResult> findAll(final LocalDate refDate) {
        final String dayOfWeek = refDate.getDayOfWeek().name();

        return client.preparedQuery(
                "SELECT p.worker_internal_id as worker_internal_id, p.worker_external_id as worker_external_id, p.work_ssid as work_ssid, " +
                    "CASE WHEN $1 = 'MONDAY' THEN p.work_start_mon ELSE (CASE WHEN $1 = 'TUESDAY' THEN p.work_start_tue ELSE (CASE WHEN $1 = 'WEDNESDAY' THEN p.work_start_wed ELSE (CASE WHEN $1 = 'THURSDAY' THEN p.work_start_thu ELSE (CASE WHEN $1 = 'FRIDAY' THEN p.work_start_fri ELSE NULL END) END) END) END) END as work_start, " +
                    "CASE WHEN $1 = 'MONDAY' THEN p.work_end_mon ELSE (CASE WHEN $1 = 'TUESDAY' THEN p.work_end_tue ELSE (CASE WHEN $1 = 'WEDNESDAY' THEN p.work_end_wed ELSE (CASE WHEN $1 = 'THURSDAY' THEN p.work_end_thu ELSE (CASE WHEN $1 = 'FRIDAY' THEN p.work_end_fri ELSE NULL END) END) END) END) END as work_end, " +
                    "CASE WHEN $1 = 'MONDAY' THEN p.lunch_start_mon ELSE (CASE WHEN $1 = 'TUESDAY' THEN p.lunch_start_tue ELSE (CASE WHEN $1 = 'WEDNESDAY' THEN p.lunch_start_wed ELSE (CASE WHEN $1 = 'THURSDAY' THEN p.lunch_start_thu ELSE (CASE WHEN $1 = 'FRIDAY' THEN p.lunch_start_fri ELSE NULL END) END) END) END) END as lunch_start, " +
                    "CASE WHEN $1 = 'MONDAY' THEN p.lunch_end_mon ELSE (CASE WHEN $1 = 'TUESDAY' THEN p.lunch_end_tue ELSE (CASE WHEN $1 = 'WEDNESDAY' THEN p.lunch_end_wed ELSE (CASE WHEN $1 = 'THURSDAY' THEN p.lunch_end_thu ELSE (CASE WHEN $1 = 'FRIDAY' THEN p.lunch_end_fri ELSE NULL END) END) END) END) END as lunch_end, " +
                    "p.work_city_code as work_city_code, c.timezone as timezone, p.company_code as company_code, " +
                    "wh.day is not null as worker_holiday, " +
                    "ch.day is not null as city_holiday " +
                    "wc.chat_id " +
                    "FROM worker_preferences p " +
                    "INNER JOIN city c ON c.code = p.work_city_code " +
                    "LEFT OUTER JOIN city_holiday ch ON ch.city_code = p.work_city_code AND ch.day = $2 " +
                    "LEFT OUTER JOIN worker_holiday wh ON wh.worker_internal_id = p.worker_internal_id AND wh.day = $2 " +
                    "LEFT OUTER JOIN worker_chat wc ON wc.worker_internal_id = p.worker_internal_id " +
                    "ORDER BY p.worker_internal_id, wc.chat_id",
                    Tuple.of(dayOfWeek, refDate)
                )
                .map(pgRowSet -> {
                    final List<WorkerCurrentPreferences> list = new ArrayList<>(pgRowSet.size());

                    WorkerCurrentPreferencesBuilder previousWorker = null;

                    for (final Row row : pgRowSet) {
                        final String currentWorkerInternalId = row.getString("worker_internal_id");

                        if (previousWorker == null || !previousWorker.getWorkerInternalId().equals(currentWorkerInternalId)) {
                            if (previousWorker != null) {
                                list.add(previousWorker.build());
                            }

                            previousWorker = WorkerCurrentPreferencesBuilder.from(refDate, row);
                        } else {
                            previousWorker = previousWorker.addChatId(row.getString("chat_id"));
                        }
                    }

                    return WorkerCurrentPreferencesMultipleResultBuilder.buildFromResult(list);
                })
                .onFailure()
                    .recoverWithItem((e) -> {
                        final String message = CommonUtils.stringFormat("There was an unexpected error getting list of all workers current preferences on '{}'", CommonDateTimeUtils.formatDateToLog(refDate));

                        LOG.error("{}. Reason: {}", message, e.getMessage());

                        return WorkerCurrentPreferencesMultipleResultBuilder.buildFromError(message);
                    });
    }

    public Uni<WorkerCurrentPreferencesSingleResult> findByChatId(final String chatId, final LocalDate refDate) {
        final String dayOfWeek = refDate.getDayOfWeek().name();

        return client.preparedQuery(
                "SELECT p.worker_internal_id as worker_internal_id, p.worker_external_id as worker_external_id, p.work_ssid as work_ssid, " +
                    "CASE WHEN $1 = 'MONDAY' THEN p.work_start_mon ELSE (CASE WHEN $1 = 'TUESDAY' THEN p.work_start_tue ELSE (CASE WHEN $1 = 'WEDNESDAY' THEN p.work_start_wed ELSE (CASE WHEN $1 = 'THURSDAY' THEN p.work_start_thu ELSE (CASE WHEN $1 = 'FRIDAY' THEN p.work_start_fri ELSE NULL END) END) END) END) END as work_start, " +
                    "CASE WHEN $1 = 'MONDAY' THEN p.work_end_mon ELSE (CASE WHEN $1 = 'TUESDAY' THEN p.work_end_tue ELSE (CASE WHEN $1 = 'WEDNESDAY' THEN p.work_end_wed ELSE (CASE WHEN $1 = 'THURSDAY' THEN p.work_end_thu ELSE (CASE WHEN $1 = 'FRIDAY' THEN p.work_end_fri ELSE NULL END) END) END) END) END as work_end, " +
                    "CASE WHEN $1 = 'MONDAY' THEN p.lunch_start_mon ELSE (CASE WHEN $1 = 'TUESDAY' THEN p.lunch_start_tue ELSE (CASE WHEN $1 = 'WEDNESDAY' THEN p.lunch_start_wed ELSE (CASE WHEN $1 = 'THURSDAY' THEN p.lunch_start_thu ELSE (CASE WHEN $1 = 'FRIDAY' THEN p.lunch_start_fri ELSE NULL END) END) END) END) END as lunch_start, " +
                    "CASE WHEN $1 = 'MONDAY' THEN p.lunch_end_mon ELSE (CASE WHEN $1 = 'TUESDAY' THEN p.lunch_end_tue ELSE (CASE WHEN $1 = 'WEDNESDAY' THEN p.lunch_end_wed ELSE (CASE WHEN $1 = 'THURSDAY' THEN p.lunch_end_thu ELSE (CASE WHEN $1 = 'FRIDAY' THEN p.lunch_end_fri ELSE NULL END) END) END) END) END as lunch_end, " +
                    "p.work_city_code as work_city_code, c.timezone as timezone, p.company_code as company_code, " +
                    "wh.day is not null as worker_holiday, " +
                    "ch.day is not null as city_holiday, " +
                    "wc.chat_id " +
                    "FROM worker_chat wc " +
                    "INNER JOIN worker_preferences p ON p.worker_internal_id = wc.worker_internal_id " +
                    "INNER JOIN city c ON c.code = p.work_city_code " +
                    "LEFT OUTER JOIN city_holiday ch ON ch.city_code = p.work_city_code AND ch.day = $2 " +
                    "LEFT OUTER JOIN worker_holiday wh ON wh.worker_internal_id = p.worker_internal_id AND wh.day = $2 " +
                    "LEFT OUTER JOIN worker_chat wcs ON wcs.worker_internal_id = wc.worker_internal_id " +
                    "WHERE wc.chat_id = $3",
                    Tuple.of(dayOfWeek, refDate, chatId)
                )
                .map(pgRowSet -> {
                    WorkerCurrentPreferencesBuilder worker = null;

                    for (final Row row : pgRowSet) {
                        if (worker == null) {
                            worker = WorkerCurrentPreferencesBuilder.from(refDate, row);
                        } else {
                            worker = worker.addChatId(row.getString("chat_id"));
                        }
                    }

                    return Optional.ofNullable(worker)
                            .map(WorkerCurrentPreferencesSingleResultBuilder::buildFromResult)
                            .orElse(null);
                })
                .onFailure()
                    .recoverWithItem((e) -> {
                        final String message = CommonUtils.stringFormat("There was an unexpected error getting current preferences corresponding to chat '{}' on '{}'", chatId, CommonDateTimeUtils.formatDateToLog(refDate));

                        LOG.error("{}. Reason: {}", message, e.getMessage());

                        return WorkerCurrentPreferencesSingleResultBuilder.buildFromError(message);
                    });
    }
}