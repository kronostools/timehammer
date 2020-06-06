package com.kronostools.timehammer.integration.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessageBuilder;
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

    private final Emitter<WorkerRegistrationRequestMessage> comunytekWorkerRegisterChannel;

    public RegistrationWorkerRouter(@Channel(Channels.COMUNYTEK_WORKER_REGISTER) final Emitter<WorkerRegistrationRequestMessage> comunytekWorkerRegisterChannel) {
        this.comunytekWorkerRegisterChannel = comunytekWorkerRegisterChannel;
    }

    @Incoming(Channels.WORKER_REGISTER_ROUTE)
    public CompletionStage<Void> routeRegistrationRequest(final Message<WorkerRegistrationRequestMessage> message) {
        final WorkerRegistrationRequestMessage registrationRequest = WorkerRegistrationRequestMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Routing registration request message '{}' to company '{}'", registrationRequest.getRegistrationRequestId(), registrationRequest.getRegistrationRequestForm().getCompanyCode().getCode());

        if (registrationRequest.getRegistrationRequestForm().getCompanyCode() == Company.COMUNYTEK) {
            return comunytekWorkerRegisterChannel.send(registrationRequest).handle(getMessageHandler(message, registrationRequest.getRegistrationRequestForm().getCompanyCode()));
        } else {
            LOG.warn("Ignored registration request message because there is no channel for company '{}'", registrationRequest.getRegistrationRequestForm().getCompanyCode().getCode());
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