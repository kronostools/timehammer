package com.kronostools.timehammer.web.resource;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.schedules.CheckHolidayPhaseBuilder;
import com.kronostools.timehammer.common.messages.schedules.CredentialPhaseBuilder;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorker;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorkerBuilder;
import com.kronostools.timehammer.common.services.TimeMachineService;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.UUID;

@RouteBase(path = "/test")
public class ToDeleteRoutes {
    private final TimeMachineService timeMachineService;
    private final Emitter<UpdateWorkersHolidayWorker> testEmitter;

    public ToDeleteRoutes(final TimeMachineService timeMachineService,
                          @Channel(Channels.HOLIDAYS_WORKER_UPDATE) final Emitter<UpdateWorkersHolidayWorker> testEmitter) {
        this.timeMachineService = timeMachineService;
        this.testEmitter = testEmitter;
    }

    // TODO: delete this test method
    @Route(path = "/sendToComunytekWorkerHoliday", methods = HttpMethod.GET)
    void timestamp(RoutingContext rc) {
        final String scheduleName = "updateWorkersHoliday";

        final UpdateWorkersHolidayWorker worker = new UpdateWorkersHolidayWorkerBuilder()
                .generated(timeMachineService.getNow())
                .executionId(UUID.randomUUID())
                .name(scheduleName)
                .batchSize(1)
                .holidayCandidate(timeMachineService.getNow().toLocalDate())
                .workerInternalId("1111-1111-1111-1111")
                .company(Company.COMUNYTEK)
                .workerExternalId("DCV")
                .credentialPhase(new CredentialPhaseBuilder()
                        .externalPassword("blablabla")
                        .build())
                .checkHolidayPhase(new CheckHolidayPhaseBuilder()
                        .holiday(Boolean.TRUE)
                        .build())
                .build();

        final HttpServerRequest request = rc.request();

        request.pause();

        testEmitter.send(worker).handleAsync((Void, e) -> {
            request.resume();

            final JsonObject result = new JsonObject();
            result.put("name", scheduleName);
            result.put("result", e == null);

            if (e != null) {
                result.put("errorMessage", e.getMessage());
            }

            rc.response().end(result.toBuffer());

            return null;
        });
    }
}