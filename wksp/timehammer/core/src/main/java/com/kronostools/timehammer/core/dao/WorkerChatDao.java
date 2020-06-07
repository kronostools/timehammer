package com.kronostools.timehammer.core.dao;

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
public class WorkerChatDao {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerChatDao.class);

    private final PgPool client;

    public WorkerChatDao(final PgPool client) {
        this.client = client;
    }

    @Transactional(TxType.MANDATORY)
    public Uni<InsertResult> insertWorkerChat(final String workerInternalId, final String chatId) {
        return client
                .preparedQuery(
                        "INSERT INTO worker_chat(internal_id, chat_id) " +
                            "VALUES ($1, $2)")
                .execute(Tuple.of(workerInternalId, chatId))
                .flatMap(pgRowSet -> {
                    LOG.debug("Inserted {} chat '{}' of worker '{}'", pgRowSet.rowCount(), chatId, workerInternalId);

                    return Uni.createFrom().item(new InsertResultBuilder()
                            .inserted(pgRowSet.rowCount())
                            .build());
                });
    }
}
