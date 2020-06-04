package com.kronostools.timehammer.web.resource;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import com.kronostools.timehammer.web.dto.Dto;
import com.kronostools.timehammer.web.dto.RegistrationRequestSummaryDto;
import com.kronostools.timehammer.web.dto.RegistrationRequestSummaryDtoBuilder;
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

@Path("/registrationSummary")
public class RegistrationSummaryResource {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationSummaryResource.class);

    private final Cache<String, StreamSubscriber> cache;

    public RegistrationSummaryResource() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build();
    }

    @Incoming(Channels.WORKER_REGISTER_NOTIFY_IN)
    public void processBatchScheduleEvent(final WorkerRegistrationRequestMessage workerRegistrationRequest) {
        final RegistrationRequestSummaryDto registrationRequestSummaryDto = new RegistrationRequestSummaryDtoBuilder()
                .workerInternalId(workerRegistrationRequest.getRegistrationRequestForm().getWorkerInternalId())
                .build();

        publishScheduleEvent(registrationRequestSummaryDto);
    }

    private void publishScheduleEvent(final RegistrationRequestSummaryDto registrationRequestSummaryDto) {
        final StreamSubscriber streamSubscriber = cache.getIfPresent(registrationRequestSummaryDto.getWorkerInternalId());

        if (streamSubscriber != null) {
            LOG.debug("Emitting registration summary to subscriber '{}'", streamSubscriber.getSubscriberId());

            streamSubscriber.getEmitter().emit(registrationRequestSummaryDto);
        } else {
            LOG.warn("No subscriber was found for registration request '{}'", registrationRequestSummaryDto.getWorkerInternalId());
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
                        LOG.info("Removing registration summary subscriber '{}'", subscriberId);

                        e.complete();

                        cache.invalidate(subscriberId);
                    });
                }, BackPressureStrategy.BUFFER);
    }
}