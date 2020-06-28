package com.kronostools.timehammer.integration.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessage;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessageBuilder;
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
public class UpdatePasswordWorkerRouter {
    private static final Logger LOG = LoggerFactory.getLogger(UpdatePasswordWorkerRouter.class);

    private final Emitter<WorkerUpdatePasswordRequestMessage> comunytekWorkerRegisterChannel;

    public UpdatePasswordWorkerRouter(@Channel(Channels.COMUNYTEK_WORKER_UPDATE_PASSWORD) final Emitter<WorkerUpdatePasswordRequestMessage> comunytekWorkerRegisterChannel) {
        this.comunytekWorkerRegisterChannel = comunytekWorkerRegisterChannel;
    }

    @Incoming(Channels.WORKER_UPDATE_PASSWORD_ROUTE)
    public CompletionStage<Void> routeRegistrationRequest(final Message<WorkerUpdatePasswordRequestMessage> message) {
        final WorkerUpdatePasswordRequestMessage updatePasswordRequest = WorkerUpdatePasswordRequestMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Routing update password request message '{}' to company '{}'", updatePasswordRequest.getRequestId(), updatePasswordRequest.getCheckUpdatePasswordRequestPhase().getCompany().name());

        if (updatePasswordRequest.getCheckUpdatePasswordRequestPhase().getCompany() == Company.COMUNYTEK) {
            return comunytekWorkerRegisterChannel.send(updatePasswordRequest).handle(getMessageHandler(message, updatePasswordRequest.getCheckUpdatePasswordRequestPhase().getCompany()));
        } else {
            LOG.warn("Ignored update password request message because there is no channel for company '{}'", updatePasswordRequest.getCheckUpdatePasswordRequestPhase().getCompany().name());
            return message.ack();
        }
    }

    private BiFunction<? super Void, Throwable, Void> getMessageHandler(final Message<?> message, final Company company) {
        return (Void, e) -> {
            if (e != null) {
                LOG.error(stringFormat("Exception while routing update password request message to '{}' channel", company.name()), e);
            } else {
                message.ack();
                LOG.debug("Routed update password request message to '{}' channel", company.name());
            }

            return null;
        };
    }
}