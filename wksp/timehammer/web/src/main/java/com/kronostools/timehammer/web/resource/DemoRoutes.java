package com.kronostools.timehammer.web.resource;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessage;
import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessageBuilder;
import com.kronostools.timehammer.common.messages.timemachine.TimeMachineEventMessage;
import com.kronostools.timehammer.common.messages.timemachine.TimeMachineEventMessageBuilder;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RouteBase(path = "/demo")
public class DemoRoutes {
    private final TimeMachineService timeMachineService;
    private final Emitter<TimeMachineEventMessage> timeMachineChannel;
    private final Map<String, Emitter<ScheduleTriggerMessage>> scheduleChannels;

    public DemoRoutes(final TimeMachineService timeMachineService,
                      @Channel(Channels.TIMEMACHINE_OUT) final Emitter<TimeMachineEventMessage> timeMachineChannel,
                      @Channel(Channels.SCHEDULE_UPDATE_HOLIDAYS) final Emitter<ScheduleTriggerMessage> updateWorkersHolidayChannel,
                      @Channel(Channels.SCHEDULE_CLEAN_HOLIDAYS) final Emitter<ScheduleTriggerMessage> cleanPastWorkersHolidaysChannel) {
        this.timeMachineService = timeMachineService;
        this.timeMachineChannel = timeMachineChannel;

        this.scheduleChannels = new HashMap<>() {{
            put("updateWorkersHoliday", updateWorkersHolidayChannel);
            put("cleanPastWorkersHolidays", cleanPastWorkersHolidaysChannel);
        }};
    }

    @Route(path = "/currentTimestamp", methods = HttpMethod.GET)
    void currentTimestamp(RoutingContext rc) {
        final JsonObject result = new JsonObject();
        result.put("timestamp", CommonDateTimeUtils.formatDateTimeToJson(CommonDateTimeUtils.getDateTimeAtZone(timeMachineService.getNow(), SupportedTimezone.EUROPE_MADRID)));

        rc.response().end(result.toBuffer());
    }

    @Route(path = "/timeTravel", methods = HttpMethod.POST)
    void timeTravel(RoutingContext rc) {
        final String newTimestampRaw = rc.getBodyAsJson().getString("timestamp");

        final TimeMachineEventMessage timeMachineEvent = new TimeMachineEventMessageBuilder()
                .generated(timeMachineService.getNow())
                .newTimestamp(CommonDateTimeUtils.parseDateTimeFromJson(newTimestampRaw))
                .timezone(SupportedTimezone.EUROPE_MADRID)
                .build();

        final HttpServerRequest request = rc.request();

        request.pause();

        timeMachineChannel.send(timeMachineEvent).handleAsync((Void, e) -> {
            request.resume();

            final JsonObject result = new JsonObject();
            result.put("result", e == null);

            rc.response().end(result.toBuffer());

            return null;
        });
    }

    @Route(path = "/triggerSchedule/{scheduleName}", methods = HttpMethod.POST)
    void triggerSchedule(RoutingContext rc) {
        final String scheduleName = rc.pathParam("scheduleName");

        final ScheduleTriggerMessage scheduleTriggerMessage = new ScheduleTriggerMessageBuilder()
                .generated(timeMachineService.getNow())
                .name(scheduleName)
                .executionId(UUID.randomUUID())
                .build();

        final HttpServerRequest request = rc.request();

        request.pause();

        scheduleChannels.get(scheduleName).send(scheduleTriggerMessage).handleAsync((Void, e) -> {
            request.resume();

            final JsonObject result = new JsonObject();
            result.put("result", e == null);

            rc.response().end(result.toBuffer());

            return null;
        });
    }

    // TODO: endpoint para recuperar la lista de trabajadores
}