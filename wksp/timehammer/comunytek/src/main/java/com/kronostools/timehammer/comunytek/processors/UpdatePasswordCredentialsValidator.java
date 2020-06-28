package com.kronostools.timehammer.comunytek.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.WorkerCredentialsResult;
import com.kronostools.timehammer.common.messages.updatePassword.CheckUpdatePasswordCredentialsPhase;
import com.kronostools.timehammer.common.messages.updatePassword.CheckUpdatePasswordCredentialsPhaseBuilder;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessage;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessageBuilder;
import com.kronostools.timehammer.comunytek.service.WorkerCredentialValidatorService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UpdatePasswordCredentialsValidator {
    private static final Logger LOG = LoggerFactory.getLogger(UpdatePasswordCredentialsValidator.class);

    private final WorkerCredentialValidatorService workerCredentialValidatorService;

    public UpdatePasswordCredentialsValidator(final WorkerCredentialValidatorService workerCredentialValidatorService) {
        this.workerCredentialValidatorService = workerCredentialValidatorService;
    }

    @Incoming(Channels.COMUNYTEK_WORKER_UPDATE_PASSWORD)
    @Outgoing(Channels.WORKER_UPDATE_PASSWORD_NOTIFY)
    public Uni<Message<WorkerUpdatePasswordRequestMessage>> checkCredentials(final Message<WorkerUpdatePasswordRequestMessage> message) {
        final WorkerUpdatePasswordRequestMessage updatePasswordRequest = WorkerUpdatePasswordRequestMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Checking credentials of worker '{}' against Comunytek ...", updatePasswordRequest.getCheckUpdatePasswordRequestPhase().getWorkerExternalId());

        return workerCredentialValidatorService.checkWorkerCredentials(updatePasswordRequest.getCheckUpdatePasswordRequestPhase().getWorkerExternalId(), updatePasswordRequest.getUpdatePasswordRequestForm().getWorkerExternalPassword())
                .flatMap(comunytekLoginResponse -> {
                    final CheckUpdatePasswordCredentialsPhase result;

                    switch (comunytekLoginResponse.getResult()) {
                        case OK:
                            result = new CheckUpdatePasswordCredentialsPhaseBuilder()
                                    .result(WorkerCredentialsResult.OK)
                                    .build();
                            break;
                        case INVALID:
                            result = new CheckUpdatePasswordCredentialsPhaseBuilder()
                                    .result(WorkerCredentialsResult.INVALID)
                                    .build();
                            break;
                        default:
                            result = new CheckUpdatePasswordCredentialsPhaseBuilder()
                                    .result(WorkerCredentialsResult.KO)
                                    .build();
                            break;
                    }

                    updatePasswordRequest.setCheckUpdatePasswordCredentialsPhase(result);

                    return Uni.createFrom().item(Message.of(updatePasswordRequest, message::ack));
                });
    }
}