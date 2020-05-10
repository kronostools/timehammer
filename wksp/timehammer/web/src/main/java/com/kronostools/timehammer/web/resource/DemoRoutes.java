package com.kronostools.timehammer.web.resource;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.messages.timemachine.TimeMachineEventMessage;
import com.kronostools.timehammer.common.services.TimeMachineService;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.time.LocalDateTime;

@RouteBase(path = "/demo")
public class DemoRoutes {
    private final TimeMachineService timeMachineService;
    private final Emitter<TimeMachineEventMessage> timeMachineChannel;

    public DemoRoutes(final TimeMachineService timeMachineService,
                      @Channel(Channels.TIMEMACHINE_OUT) final Emitter<TimeMachineEventMessage> timeMachineChannel) {
        this.timeMachineService = timeMachineService;
        this.timeMachineChannel = timeMachineChannel;
    }

    @Route(path = "/timeTravel", methods = HttpMethod.GET)
    void timestamp(RoutingContext rc) {
        final TimeMachineEventMessage timeMachineEvent = TimeMachineEventMessage.Builder.builder()
                .generated(timeMachineService.getNow())
                .withNewTimestamp(LocalDateTime.now().withHour(17).withMinute(0).withSecond(0).withNano(0))
                .atTimezone(SupportedTimezone.EUROPE_MADRID)
                .build();

        final HttpServerRequest request = rc.request();

        request.pause();

        timeMachineChannel.send(timeMachineEvent).handleAsync((Void, e) -> {
            request.resume();

            final JsonObject result = new JsonObject();
            result.put("result", e != null);

            rc.response().end(result.toBuffer());

            return null;
        });
    }

    // TODO: endpoint para recuperar las zonas horarias soportadas

    // TODO: endpoint para recuperar la lista de trabajadores
}