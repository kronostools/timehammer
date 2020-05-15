package com.kronostools.timehammer.integration.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorker;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorkerBuilder;
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
public class HolidaysWorkerRouter {
    private static final Logger LOG = LoggerFactory.getLogger(HolidaysWorkerRouter.class);

    private final Emitter<UpdateWorkersHolidayWorker> comunytekWorkerHolidays;

    public HolidaysWorkerRouter(@Channel(Channels.COMUNYTEK_WORKER_HOLIDAYS) final Emitter<UpdateWorkersHolidayWorker> comunytekWorkerHolidays) {
        this.comunytekWorkerHolidays = comunytekWorkerHolidays;
    }

    @Incoming(Channels.HOLIDAYS_WORKER_GET)
    public CompletionStage<Void> routeHolidays(final Message<UpdateWorkersHolidayWorker> message) {
        final UpdateWorkersHolidayWorker worker = UpdateWorkersHolidayWorkerBuilder.copy(message.getPayload()).build();

        LOG.info("Routing message of worker '{}' to company '{}'", worker.getWorkerInternalId(), worker.getCompany().getCode());

        if (worker.getCompany() == Company.COMUNYTEK) {
            return comunytekWorkerHolidays.send(worker).handle(getMessageHandler(message, worker.getCompany()));
        } else {
            LOG.warn("Ignored message because there is no channel for company '{}'", worker.getCompany().getCode());
            return message.ack();
        }
    }

    private BiFunction<? super Void, Throwable, Void> getMessageHandler(final Message<?> message, final Company company) {
        return (Void, e) -> {
            if (e != null) {
                LOG.error(stringFormat("Exception while routing message to '{}' channel", company.getCode()), e);
            } else {
                message.ack();
                LOG.debug("Routed message to '{}' channel", company.getCode());
            }

            return null;
        };
    }
}