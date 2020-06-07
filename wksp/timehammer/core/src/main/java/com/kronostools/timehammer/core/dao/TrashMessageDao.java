package com.kronostools.timehammer.core.dao;

import com.kronostools.timehammer.common.utils.CommonUtils;
import com.kronostools.timehammer.core.constants.TrashMessageStatus;
import com.kronostools.timehammer.core.model.UpsertResult;
import com.kronostools.timehammer.core.model.UpsertResultBuilder;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class TrashMessageDao {
    private static final Logger LOG = LoggerFactory.getLogger(TrashMessageDao.class);

    private final PgPool client;

    public TrashMessageDao(final PgPool client) {
        this.client = client;
    }

    public Uni<UpsertResult> insertMessage(final String chatId, final LocalDateTime timestamp, final String text) {
        return client
                .preparedQuery(
                        "INSERT INTO trash_message (chat_id, timestamp, status, text) " +
                            "VALUES ($1, $2, $3, $4) " +
                            "ON CONFLICT DO NOTHING")
                .execute(Tuple.of(chatId, timestamp, TrashMessageStatus.UNCHECKED.name(), text))
                .map(pgRowSet -> new UpsertResultBuilder()
                            .inserted(pgRowSet.rowCount())
                            .build())
                .onFailure()
                    .recoverWithItem((e) -> {
                        final String message = CommonUtils.stringFormat("There was an unexpected error inserting trash message");

                        LOG.error("{}. Reason: {}", message, e.getMessage());

                        return new UpsertResultBuilder()
                                .errorMessage(message)
                                .build();
                    });
    }
}