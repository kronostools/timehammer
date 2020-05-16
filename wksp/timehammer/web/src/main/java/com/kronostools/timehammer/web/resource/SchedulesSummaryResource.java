package com.kronostools.timehammer.web.resource;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.BatchScheduleSummaryMessage;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.web.dto.Dto;
import com.kronostools.timehammer.web.dto.ScheduleSummaryDto;
import com.kronostools.timehammer.web.dto.ScheduleSummaryDtoBuilder;
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

@Path("/schedulesSummary")
public class SchedulesSummaryResource {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulesSummaryResource.class);

    private final Cache<String, StreamSubscriber> cache;

    public SchedulesSummaryResource() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build();
    }

    @Incoming(Channels.SCHEDULE_SUMMARY)
    public void processStreamEvent(final BatchScheduleSummaryMessage batchScheduleSummaryMessage) {
        final ScheduleSummaryDto scheduleSummaryDto = new ScheduleSummaryDtoBuilder()
                .name(batchScheduleSummaryMessage.getName())
                .startTimestamp(CommonDateTimeUtils.formatDateTimeToJson(batchScheduleSummaryMessage.getTimestamp()))
                .endTimestamp(CommonDateTimeUtils.formatDateTimeToJson(batchScheduleSummaryMessage.getEndTimestamp()))
                .totalItemsProcessed(batchScheduleSummaryMessage.getBatchSize())
                .itemsProcessedOk(batchScheduleSummaryMessage.getProcessedOk())
                .itemsProcessedKo(batchScheduleSummaryMessage.getProcessedKo())
                .build();

        cache.asMap().values().forEach(sb -> {
            LOG.debug("Emitting schedule summary event to subscriber '{}'", sb.getSubscriberId());

            sb.getEmitter().emit(scheduleSummaryDto);
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
                        LOG.info("Removing schedule summary subscriber '{}'", subscriberId);

                        e.complete();

                        cache.invalidate(subscriberId);
                    });
                }, BackPressureStrategy.BUFFER);
    }
}