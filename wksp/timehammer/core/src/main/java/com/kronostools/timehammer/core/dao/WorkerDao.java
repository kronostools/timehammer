package com.kronostools.timehammer.core.dao;

import com.kronostools.timehammer.core.constants.WorkerProfile;
import com.kronostools.timehammer.core.model.InsertResult;
import com.kronostools.timehammer.core.model.InsertResultBuilder;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerDao {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerDao.class);

    private final PgPool client;

    public WorkerDao(final PgPool client) {
        this.client = client;
    }

    public Uni<InsertResult> insertWorker(final String workerInternalId, final String fullname) {
        return client
                .preparedQuery(
                        "INSERT INTO worker(internal_id, full_name, profile) " +
                            "VALUES ($1, $2, $3)", Tuple.of(workerInternalId, fullname, WorkerProfile.WORKER.name()))
                .map((pgRowSet) -> new InsertResultBuilder()
                        .inserted(pgRowSet.rowCount())
                        .build());
    }
}