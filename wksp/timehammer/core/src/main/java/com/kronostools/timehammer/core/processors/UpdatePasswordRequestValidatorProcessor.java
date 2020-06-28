package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.ValidationError;
import com.kronostools.timehammer.common.messages.ValidationErrorBuilder;
import com.kronostools.timehammer.common.messages.updatePassword.ValidateUpdatePasswordRequestPhase;
import com.kronostools.timehammer.common.messages.updatePassword.ValidateUpdatePasswordRequestPhaseBuilder;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessage;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessageBuilder;
import com.kronostools.timehammer.common.messages.updatePassword.forms.UpdatePasswordRequestForm;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

import static com.kronostools.timehammer.common.utils.CommonUtils.stringFormat;

@ApplicationScoped
public class UpdatePasswordRequestValidatorProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(UpdatePasswordRequestValidatorProcessor.class);

    private final Emitter<WorkerUpdatePasswordRequestMessage> workerUpdatePasswordRouteChannel;
    private final Emitter<WorkerUpdatePasswordRequestMessage> workerUpdatePasswordNotifyChannel;

    public UpdatePasswordRequestValidatorProcessor(@Channel(Channels.WORKER_UPDATE_PASSWORD_VALIDATE_NOTIFY) final Emitter<WorkerUpdatePasswordRequestMessage> workerUpdatePasswordNotifyChannel,
                                                   @Channel(Channels.WORKER_UPDATE_PASSWORD_ROUTE) final Emitter<WorkerUpdatePasswordRequestMessage> workerUpdatePasswordRouteChannel) {
        this.workerUpdatePasswordRouteChannel = workerUpdatePasswordRouteChannel;
        this.workerUpdatePasswordNotifyChannel = workerUpdatePasswordNotifyChannel;
    }

    @Incoming(Channels.WORKER_UPDATE_PASSWORD_VALIDATE)
    public CompletionStage<Void> process(final Message<WorkerUpdatePasswordRequestMessage> message) {
        final WorkerUpdatePasswordRequestMessage updatePasswordRequest = WorkerUpdatePasswordRequestMessageBuilder.copy(message.getPayload()).build();

        if (updatePasswordRequest.getCheckUpdatePasswordRequestPhase().isNotSuccessful()) {
            LOG.info("Nothing to validate because update password request '{}' is expired -> routing it to the end step in update password flow: '{}' ...", updatePasswordRequest.getRequestId(), Channels.WORKER_REGISTER_VALIDATE_NOTIFY);

            return workerUpdatePasswordNotifyChannel.send(updatePasswordRequest)
                    .handle(getMessageHandler(message, updatePasswordRequest.getRequestId()));
        } else {
            LOG.info("Validating update password request '{}' ...", updatePasswordRequest.getRequestId());

            final List<ValidationError> validationErrors = validateForm(updatePasswordRequest.getUpdatePasswordRequestForm());

            final ValidateUpdatePasswordRequestPhase validationResult = new ValidateUpdatePasswordRequestPhaseBuilder()
                    .validationErrors(validationErrors)
                    .build();

            updatePasswordRequest.setValidateUpdatePasswordRequestPhase(validationResult);

            if (validationResult.isSuccessful()) {
                LOG.info("Update password request '{}' is valid -> routing it to the next step in update password flow: '{}' ...", updatePasswordRequest.getRequestId(), Channels.WORKER_UPDATE_PASSWORD_ROUTE);

                return workerUpdatePasswordRouteChannel.send(updatePasswordRequest)
                        .handle(getMessageHandler(message, updatePasswordRequest.getRequestId()));
            } else {
                LOG.info("Update password request '{}' is invalid -> routing it to the end step in update password flow: '{}' ...", updatePasswordRequest.getRequestId(), Channels.WORKER_UPDATE_PASSWORD_VALIDATE_NOTIFY);

                return workerUpdatePasswordNotifyChannel.send(updatePasswordRequest)
                        .handle(getMessageHandler(message, updatePasswordRequest.getRequestId()));
            }
        }
    }

    private BiFunction<? super Void, Throwable, Void> getMessageHandler(final Message<?> message, final String requestId) {
        return (Void, e) -> {
            if (e != null) {
                LOG.error(stringFormat("Exception while routing update password request '{}'", requestId), e);
            } else {
                message.ack();
                LOG.debug("Update password request '{}' routed successfully", requestId);
            }

            return null;
        };
    }

    private List<ValidationError> validateForm(final UpdatePasswordRequestForm updatePasswordRequestForm) {
        final List<ValidationError> validationErrors = new ArrayList<>();

        if (StringUtils.isBlank(updatePasswordRequestForm.getWorkerExternalPassword())) {
            validationErrors.add(new ValidationErrorBuilder()
                    .fieldName("workerExternalPassword")
                    .errorMessage("La contraseña es obligatoria")
                    .build());
        }

        return validationErrors;
    }
}