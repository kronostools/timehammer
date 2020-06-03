package com.kronostools.timehammer.core.dao;

import com.kronostools.timehammer.core.model.InsertResult;
import com.kronostools.timehammer.core.model.InsertResultBuilder;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerChatDao {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerChatDao.class);

    private final PgPool client;

    public WorkerChatDao(final PgPool client) {
        this.client = client;
    }

    public Uni<InsertResult> insertWorkerChat(final String workerInternalId, final String chatId) {
        return client
                .preparedQuery(
                        "INSERT INTO worker_chat(internal_id, chat_id) " +
                            "VALUES ($1, $2)", Tuple.of(workerInternalId, chatId))
                .map((pgRowSet) -> new InsertResultBuilder()
                        .inserted(pgRowSet.rowCount())
                        .build());
    }
}
