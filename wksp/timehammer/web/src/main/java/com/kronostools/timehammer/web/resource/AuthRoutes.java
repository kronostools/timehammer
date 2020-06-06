package com.kronostools.timehammer.web.resource;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessageBuilder;
import com.kronostools.timehammer.common.messages.registration.forms.RegistrationRequestForm;
import com.kronostools.timehammer.common.services.TimeMachineService;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@RouteBase(path = "/auth")
public class AuthRoutes {
    private final TimeMachineService timeMachineService;
    private final Emitter<WorkerRegistrationRequestMessage> workerRegistrationRequestMessageEmitter;

    public AuthRoutes(final TimeMachineService timeMachineService,
                      @Channel(Channels.WORKER_REGISTER_INIT) final Emitter<WorkerRegistrationRequestMessage> workerRegistrationRequestMessageEmitter) {
        this.timeMachineService = timeMachineService;
        this.workerRegistrationRequestMessageEmitter = workerRegistrationRequestMessageEmitter;
    }

    @Route(path = "/register", methods = HttpMethod.POST)
    void timeTravel(RoutingContext rc) {
        final JsonObject body = rc.getBodyAsJson();
        final String registrationRequestId = body.getString("registrationRequestId");
        final RegistrationRequestForm registrationRequestForm = body.getJsonObject("registrationRequest").mapTo(RegistrationRequestForm.class);

        final WorkerRegistrationRequestMessage registrationRequest = new WorkerRegistrationRequestMessageBuilder()
                .generated(timeMachineService.getNow())
                .registrationRequestId(registrationRequestId)
                .registrationRequestForm(registrationRequestForm)
                .build();

        final HttpServerRequest request = rc.request();

        request.pause();

        workerRegistrationRequestMessageEmitter.send(registrationRequest).handleAsync((Void, e) -> {
            request.resume();

            final JsonObject result = new JsonObject();
            result.put("result", e == null);

            rc.response().end(result.toBuffer());

            return null;
        });
    }
}