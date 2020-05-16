package com.kronostools.timehammer.web.resource;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.SupportedTimezone;
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

@RouteBase(path = "/demo")
public class DemoRoutes {
    private final TimeMachineService timeMachineService;
    private final Emitter<TimeMachineEventMessage> timeMachineChannel;

    public DemoRoutes(final TimeMachineService timeMachineService,
                      @Channel(Channels.TIMEMACHINE_OUT) final Emitter<TimeMachineEventMessage> timeMachineChannel) {
        this.timeMachineService = timeMachineService;
        this.timeMachineChannel = timeMachineChannel;
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
                .timestamp(timeMachineService.getNow())
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

    // TODO: endpoint para recuperar la lista de trabajadores
}