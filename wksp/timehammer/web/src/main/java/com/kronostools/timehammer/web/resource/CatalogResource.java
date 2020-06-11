package com.kronostools.timehammer.web.resource;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.catalog.CatalogMessage;
import com.kronostools.timehammer.web.dto.*;
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
import java.util.stream.Collectors;

@Path("/catalog")
public class CatalogResource {
    private static final Logger LOG = LoggerFactory.getLogger(CatalogResource.class);

    private final Cache<String, StreamSubscriber> cache;

    public CatalogResource() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build();
    }

    @Incoming(Channels.CATALOG_RESPONSE)
    public void processBatchScheduleEvent(final CatalogMessage catalogMessage) {
        final CatalogRequestDtoBuilder catalogRequestDtoBuilder = new CatalogRequestDtoBuilder()
                .requesterId(catalogMessage.getRequesterId());

        catalogMessage.getCatalogResponses().forEach((ct, crp) -> {
            catalogRequestDtoBuilder.putCatalogResponse(ct, new CatalogResponseDtoBuilder(ct, crp.getResult())
                    .elements(crp.getElements().stream()
                            .map(ce -> new CatalogElementDto(ce.getCode(), ce.getLabel()))
                            .collect(Collectors.toList()))
                    .build());
        });

        publishScheduleEvent(catalogRequestDtoBuilder.build());
    }

    private void publishScheduleEvent(final CatalogRequestDto catalogRequestDto) {
        final StreamSubscriber streamSubscriber = cache.getIfPresent(catalogRequestDto.getRequesterId());

        if (streamSubscriber != null) {
            LOG.debug("Emitting catalog response to subscriber '{}'", streamSubscriber.getSubscriberId());

            streamSubscriber.getEmitter().emit(catalogRequestDto);
        } else {
            LOG.warn("No subscriber was found for catalog requester '{}'", catalogRequestDto.getRequesterId());
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
                        LOG.info("Removing catalog subscriber '{}'", subscriberId);

                        e.complete();

                        cache.invalidate(subscriberId);
                    });
                }, BackPressureStrategy.BUFFER);
    }
}