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
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@ApplicationScoped
public class WorkerDao {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerDao.class);

    private final PgPool client;

    public WorkerDao(final PgPool client) {
        this.client = client;
    }

    @Transactional(TxType.MANDATORY)
    public Uni<InsertResult> insertWorker(final String workerInternalId, final String fullname) {
        return client
                .preparedQuery(
                        "INSERT INTO worker(internal_id, full_name, profile) " +
                            "VALUES ($1, $2, $3)")
                .execute(Tuple.of(workerInternalId, fullname, WorkerProfile.WORKER.name()))
                .flatMap(pgRowSet -> {
                    LOG.debug("Inserted {} worker with internal_id '{}'", pgRowSet.rowCount(), workerInternalId);

                    return Uni.createFrom().item(new InsertResultBuilder()
                            .inserted(pgRowSet.rowCount())
                            .build());
                });
    }
}