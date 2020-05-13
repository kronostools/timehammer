package com.kronostools.timehammer.core.dao;

import com.kronostools.timehammer.common.utils.CommonUtils;
import com.kronostools.timehammer.core.model.UpsertResult;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;

@ApplicationScoped
public class WorkerHolidaysDao {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerHolidaysDao.class);

    private final PgPool client;

    public WorkerHolidaysDao(final PgPool client) {
        this.client = client;
    }

    public Uni<UpsertResult> upsert(final String workerInternalId, final LocalDate holidayCandidate) {
        return client
                .preparedQuery(
                        "INSERT INTO worker_holiday (worker_internal_id, day) " +
                            "VALUES ($1, $2) " +
                            "ON CONFLICT DO NOTHING", Tuple.of(workerInternalId, holidayCandidate))
                .map((pgRowSet) -> UpsertResult.Builder.builder()
                        .inserted(pgRowSet.iterator().next().getLong(2))
                        .build())
                .onFailure()
                    .recoverWithItem((e) -> {
                        final String message = CommonUtils.stringFormat("There was an unexpected error inserting holidays of worker '{}'. Reason: {}", workerInternalId);

                        LOG.error("{}. Reason: {}", message, e.getMessage());

                        return UpsertResult.Builder.builder()
                                .buildUnsuccessful(message);
                    });
    }
}