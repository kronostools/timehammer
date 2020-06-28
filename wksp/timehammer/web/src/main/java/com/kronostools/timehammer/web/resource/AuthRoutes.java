package com.kronostools.timehammer.web.resource;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessageBuilder;
import com.kronostools.timehammer.common.messages.registration.forms.RegistrationRequestForm;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessage;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessageBuilder;
import com.kronostools.timehammer.common.messages.updatePassword.forms.UpdatePasswordRequestForm;
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
    private final Emitter<WorkerUpdatePasswordRequestMessage> workerUpdatePasswordRequestMessageEmitter;

    public AuthRoutes(final TimeMachineService timeMachineService,
                      @Channel(Channels.WORKER_REGISTER_INIT) final Emitter<WorkerRegistrationRequestMessage> workerRegistrationRequestMessageEmitter,
                      @Channel(Channels.WORKER_UPDATE_PASSWORD_INIT) final Emitter<WorkerUpdatePasswordRequestMessage> workerUpdatePasswordRequestMessageEmitter) {
        this.timeMachineService = timeMachineService;
        this.workerRegistrationRequestMessageEmitter = workerRegistrationRequestMessageEmitter;
        this.workerUpdatePasswordRequestMessageEmitter = workerUpdatePasswordRequestMessageEmitter;
    }

    @Route(path = "/register", methods = HttpMethod.POST)
    void register(RoutingContext rc) {
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

    @Route(path = "/updatePassword", methods = HttpMethod.POST)
    void updatePassword(RoutingContext rc) {
        final JsonObject body = rc.getBodyAsJson();
        final String internalId = body.getString("internalId");
        final UpdatePasswordRequestForm updatePasswordRequestForm = body.getJsonObject("updatePasswordRequest").mapTo(UpdatePasswordRequestForm.class);

        final WorkerUpdatePasswordRequestMessage updatePasswordRequest = new WorkerUpdatePasswordRequestMessageBuilder()
                .generated(timeMachineService.getNow())
                .requestId(internalId)
                .updatePasswordRequestForm(updatePasswordRequestForm)
                .build();

        final HttpServerRequest request = rc.request();

        request.pause();

        workerUpdatePasswordRequestMessageEmitter.send(updatePasswordRequest).handleAsync((Void, e) -> {
            request.resume();

            final JsonObject result = new JsonObject();
            result.put("result", e == null);

            rc.response().end(result.toBuffer());

            return null;
        });
    }
}