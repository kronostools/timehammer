package com.kronostools.timehammer.web.resource;

import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.web.dto.TimestampDto;
import io.smallrye.mutiny.Multi;
import org.jboss.resteasy.annotations.SseElementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Duration;

@Path("/timemachine")
public class TimeMachineResource {
    private static final Logger LOG = LoggerFactory.getLogger(TimeMachineResource.class);

    private final TimeMachineService timeMachineService;

    public TimeMachineResource(final TimeMachineService timeMachineService) {
        this.timeMachineService = timeMachineService;
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Multi<TimestampDto> stream() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .onItem().apply(n -> TimestampDto.Builder.builder()
                        .timestamp(CommonDateTimeUtils.formatDateTimeToJson(timeMachineService.getNow()))
                        .timezone(CommonDateTimeUtils.formatTimezoneToJson(timeMachineService.getTimezone()))
                        .build());
    }

    @GET
    @Path("/current")
    @Produces(MediaType.APPLICATION_JSON)
    public TimestampDto getCurrentTimestamp() {
        return TimestampDto.Builder.builder()
                .timestamp(CommonDateTimeUtils.formatDateTimeToJson(timeMachineService.getNow()))
                .timezone(CommonDateTimeUtils.formatTimezoneToJson(timeMachineService.getTimezone()))
                .build();
    }
}