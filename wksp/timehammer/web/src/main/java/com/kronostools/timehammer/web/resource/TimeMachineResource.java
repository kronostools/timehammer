package com.kronostools.timehammer.web.resource;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.messages.timemachine.TimeMachineEventMessage;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.web.dto.Dto;
import com.kronostools.timehammer.web.dto.TimestampDto;
import com.kronostools.timehammer.web.dto.TimestampDtoBuilder;
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
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Path("/timemachine")
public class TimeMachineResource {
    private static final Logger LOG = LoggerFactory.getLogger(TimeMachineResource.class);

    private final Cache<String, StreamSubscriber> cache;

    public TimeMachineResource() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build();
    }

    @Incoming(Channels.TIMEMACHINE_STREAM)
    public void processStreamEvent(final TimeMachineEventMessage timeMachineEventMessage) {
        final LocalDateTime timestamp = CommonDateTimeUtils.getDateTimeWithZone(timeMachineEventMessage.getNewTimestamp(), timeMachineEventMessage.getTimezone());

        final TimestampDto timestampDto = new TimestampDtoBuilder()
                .timestamp(CommonDateTimeUtils.formatDateTimeToJson(CommonDateTimeUtils.getDateTimeAtZone(timestamp, SupportedTimezone.EUROPE_MADRID)))
                .build();

        cache.asMap().values().forEach(sb -> {
            LOG.debug("Emitting timemachine event to subscriber '{}'", sb.getSubscriberId());

            sb.getEmitter().emit(timestampDto);
        });
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
                        LOG.info("Removing timemachine subscriber '{}'", subscriberId);

                        e.complete();

                        cache.invalidate(subscriberId);
                    });
                }, BackPressureStrategy.IGNORE);
    }
}