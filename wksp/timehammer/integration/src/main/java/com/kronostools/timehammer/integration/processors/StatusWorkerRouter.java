package com.kronostools.timehammer.integration.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersStatusWorker;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersStatusWorkerBuilder;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

import static com.kronostools.timehammer.common.utils.CommonUtils.stringFormat;

@ApplicationScoped
public class StatusWorkerRouter {
    private static final Logger LOG = LoggerFactory.getLogger(StatusWorkerRouter.class);

    private final Emitter<UpdateWorkersStatusWorker> comunytekWorkerStatusChannel;

    public StatusWorkerRouter(@Channel(Channels.COMUNYTEK_WORKER_STATUS) final Emitter<UpdateWorkersStatusWorker> comunytekWorkerStatusChannel) {
        this.comunytekWorkerStatusChannel = comunytekWorkerStatusChannel;
    }

    @Incoming(Channels.STATUS_WORKER_GET)
    public CompletionStage<Void> routeHolidays(final Message<UpdateWorkersStatusWorker> message) {
        final UpdateWorkersStatusWorker worker = UpdateWorkersStatusWorkerBuilder.copy(message.getPayload()).build();

        LOG.info("Routing update status message of worker '{}' to company '{}'", worker.getWorkerInternalId(), worker.getCompany().getCode());

        if (worker.getCompany() == Company.COMUNYTEK) {
            return comunytekWorkerStatusChannel.send(worker).handle(getMessageHandler(message, worker.getCompany()));
        } else {
            LOG.warn("Ignored update status message because there is no channel for company '{}'", worker.getCompany().getCode());
            return message.ack();
        }
    }

    private BiFunction<? super Void, Throwable, Void> getMessageHandler(final Message<?> message, final Company company) {
        return (Void, e) -> {
            if (e != null) {
                LOG.error(stringFormat("Exception while routing update status message to '{}' channel", company.getCode()), e);
            } else {
                message.ack();
                LOG.debug("Routed update status message to '{}' channel", company.getCode());
            }

            return null;
        };
    }
}