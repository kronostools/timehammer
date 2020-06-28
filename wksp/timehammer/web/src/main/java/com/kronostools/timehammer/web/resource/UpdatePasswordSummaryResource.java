package com.kronostools.timehammer.web.resource;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessage;
import com.kronostools.timehammer.web.dto.Dto;
import com.kronostools.timehammer.web.dto.UpdatePasswordRequestSummaryDto;
import com.kronostools.timehammer.web.dto.UpdatePasswordRequestSummaryDtoBuilder;
import com.kronostools.timehammer.web.dto.ValidationErrorDtoBuilder;
import com.kronostools.timehammer.web.model.StreamSubscriber;
import com.kronostools.timehammer.web.model.StreamSubscriberBuilder;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.BackPressureStrategy;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.resteasy.annotations.SseElementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.TimeUnit;

@Path("/updatePasswordSummary")
public class UpdatePasswordSummaryResource {
    private static final Logger LOG = LoggerFactory.getLogger(UpdatePasswordSummaryResource.class);

    private final Cache<String, StreamSubscriber> cache;

    public UpdatePasswordSummaryResource() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build();
    }

    @Incoming(Channels.WORKER_UPDATE_PASSWORD_NOTIFY)
    public void processBatchScheduleEvent(final WorkerUpdatePasswordRequestMessage updatePasswordRequest) {
        final UpdatePasswordRequestSummaryDtoBuilder updatePasswordRequestSummaryDto = new UpdatePasswordRequestSummaryDtoBuilder()
                .registrationRequestId(updatePasswordRequest.getRequestId());

        if (updatePasswordRequest.getCheckUpdatePasswordRequestPhase().isNotSuccessful()) {
            updatePasswordRequestSummaryDto.addValidationError(new ValidationErrorDtoBuilder()
                    .errorMessage("La solicitud de cambio de contraseña ha expirado, por favor, vuelva a iniciar otra desde Telegram")
                    .errorCode(500)
                    .build());
        } else {
            if (updatePasswordRequest.getValidateUpdatePasswordRequestPhase().isNotSuccessful()) {
                updatePasswordRequest.getValidateUpdatePasswordRequestPhase().getValidationErrors().stream()
                        .map(ValidationErrorDtoBuilder::copyAndBuild)
                        .forEach(updatePasswordRequestSummaryDto::addValidationError);
            } else {
                if (updatePasswordRequest.getCheckUpdatePasswordCredentialsPhase().isNotSuccessful()) {
                    switch (updatePasswordRequest.getCheckUpdatePasswordCredentialsPhase().getResult()) {
                        case INVALID:
                            updatePasswordRequestSummaryDto.addValidationError(new ValidationErrorDtoBuilder()
                                    .fieldName("workerExternalPassword")
                                    .errorMessage("La contraseña introducida no es correcta")
                                    .build());
                            break;
                        case KO:
                            updatePasswordRequestSummaryDto.addValidationError(new ValidationErrorDtoBuilder()
                                    .errorMessage("Ha ocurrido un error inesperado y no se ha podido registrar el cambio de contraseña. Por favor, inténtelo de nuevo, y si el error persiste, contacte con el administrador del sitio")
                                    .build());
                            break;
                    }
                }
            }
        }

        publishUpdatePasswordSummaryEvent(updatePasswordRequestSummaryDto.build());
    }

    private void publishUpdatePasswordSummaryEvent(final UpdatePasswordRequestSummaryDto updatePasswordRequestSummaryDto) {
        final StreamSubscriber streamSubscriber = cache.getIfPresent(updatePasswordRequestSummaryDto.getRequestId());

        if (streamSubscriber != null) {
            LOG.debug("Emitting update password summary to subscriber '{}'", streamSubscriber.getSubscriberId());

            streamSubscriber.getEmitter().emit(updatePasswordRequestSummaryDto);
        } else {
            LOG.warn("No subscriber was found for update password request '{}'", updatePasswordRequestSummaryDto.getRequestId());
        }
    }

    @GET
    @Path("/stream/{subscriberId}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Multi<Dto> stream(@PathParam("subscriberId") final String subscriberId) {
        final StreamSubscriber streamSubscriber = cache.get(subscriberId, sid -> new StreamSubscriberBuilder().subscriberId(sid).build());

        return Multi.createFrom()
                .emitter(e -> {
                    streamSubscriber.setEmitter(e);

                    e.onTermination(() -> {
                        LOG.info("Removing update password summary subscriber '{}'", subscriberId);

                        e.complete();

                        cache.invalidate(subscriberId);
                    });
                }, BackPressureStrategy.BUFFER);
    }
}