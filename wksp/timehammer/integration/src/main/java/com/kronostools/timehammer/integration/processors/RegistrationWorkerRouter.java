package com.kronostools.timehammer.integration.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequest;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestBuilder;
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
public class RegistrationWorkerRouter {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationWorkerRouter.class);

    private final Emitter<WorkerRegistrationRequest> comunytekWorkerRegisterChannel;

    public RegistrationWorkerRouter(@Channel(Channels.COMUNYTEK_WORKER_REGISTER) final Emitter<WorkerRegistrationRequest> comunytekWorkerRegisterChannel) {
        this.comunytekWorkerRegisterChannel = comunytekWorkerRegisterChannel;
    }

    @Incoming(Channels.HOLIDAYS_WORKER_GET)
    public CompletionStage<Void> routeRegistrationRequest(final Message<WorkerRegistrationRequest> message) {
        final WorkerRegistrationRequest registrationRequest = WorkerRegistrationRequestBuilder.copy(message.getPayload()).build();

        LOG.info("Routing registration request message '{}' to company '{}'", registrationRequest.getRegistrationRequestForm().getWorkerInternalId(), registrationRequest.getRegistrationRequestForm().getCompany().getCode());

        if (registrationRequest.getRegistrationRequestForm().getCompany() == Company.COMUNYTEK) {
            return comunytekWorkerRegisterChannel.send(registrationRequest).handle(getMessageHandler(message, registrationRequest.getRegistrationRequestForm().getCompany()));
        } else {
            LOG.warn("Ignored registration request message because there is no channel for company '{}'", registrationRequest.getRegistrationRequestForm().getCompany().getCode());
            return message.ack();
        }
    }

    private BiFunction<? super Void, Throwable, Void> getMessageHandler(final Message<?> message, final Company company) {
        return (Void, e) -> {
            if (e != null) {
                LOG.error(stringFormat("Exception while routing registration request message to '{}' channel", company.getCode()), e);
            } else {
                message.ack();
                LOG.debug("Routed registration request message to '{}' channel", company.getCode());
            }

            return null;
        };
    }
}