package com.kronostools.timehammer.web.resource;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.schedules.CheckHolidayResult;
import com.kronostools.timehammer.common.messages.schedules.CredentialResult;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidaysWorker;
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
    private final Emitter<UpdateWorkersHolidaysWorker> testEmitter;

    public ToDeleteRoutes(final TimeMachineService timeMachineService,
                          @Channel(Channels.HOLIDAYS_WORKER_UPDATE) final Emitter<UpdateWorkersHolidaysWorker> testEmitter) {
        this.timeMachineService = timeMachineService;
        this.testEmitter = testEmitter;
    }

    // TODO: delete this test method
    @Route(path = "/sendToComunytekWorkerHoliday", methods = HttpMethod.GET)
    void timestamp(RoutingContext rc) {
        final UpdateWorkersHolidaysWorker worker = UpdateWorkersHolidaysWorker.Builder.builder()
                .timestamp(timeMachineService.getNow())
                .company(Company.COMUNYTEK)
                .name("updateWorkersHolidays")
                .executionId(UUID.randomUUID())
                .workerInternalId("1111-1111-1111-1111")
                .workerExternalId("DCV")
                .batchSize(1)
                .build();

        worker.setCredentialResult(CredentialResult.Builder.builder()
                .externalPassword("blablabla")
                .build());

        worker.setCheckHolidayResult(CheckHolidayResult.Builder.builder()
                .holiday(Boolean.TRUE)
                .build());

        final HttpServerRequest request = rc.request();

        request.pause();

        testEmitter.send(worker).handleAsync((Void, e) -> {
            request.resume();

            final JsonObject result = new JsonObject();
            result.put("result", e != null);

            rc.response().end(result.toBuffer());

            return null;
        });
    }
}