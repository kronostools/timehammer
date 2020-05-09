package com.kronostools.timehammer.web.resource;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.timemachine.TimeMachineEventMessage;
import io.quarkus.vertx.web.RouteBase;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@RouteBase(path = "/demo")
public class DemoRoutes {
    private final Emitter<TimeMachineEventMessage> timeMachineChannel;

    public DemoRoutes(@Channel(Channels.TIMEMACHINE_OUT) final Emitter<TimeMachineEventMessage> timeMachineChannel) {
        this.timeMachineChannel = timeMachineChannel;
    }

//    @Route(path = "/timestamp", methods = HttpMethod.GET)
//    void timestamp(RoutingContext rc) {
//        rc.getBodyAsJson();
//        JsonObject jo = new JsonObject();
//        rc.response().end("hello");
//    }

    // TODO: endpoint para recuperar las zonas horarias soportadas

    // TODO: endpoint para recuperar la lista de trabajadores
}