package com.kronostools.timehammer.web.resource;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
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
    private final Emitter<UpdateWorkersHolidaysWorker> updateWorkersHolidaysWorkerEmitter;

    public ToDeleteRoutes(final TimeMachineService timeMachineService,
                          @Channel(Channels.COMUNYTEK_WORKER_HOLIDAYS) final Emitter<UpdateWorkersHolidaysWorker> updateWorkersHolidaysWorkerEmitter) {
        this.timeMachineService = timeMachineService;
        this.updateWorkersHolidaysWorkerEmitter = updateWorkersHolidaysWorkerEmitter;
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

        worker.setExternalPassword("blablabla");

        final HttpServerRequest request = rc.request();

        request.pause();

        updateWorkersHolidaysWorkerEmitter.send(worker).handleAsync((Void, e) -> {
            request.resume();

            final JsonObject result = new JsonObject();
            result.put("result", e != null);

            rc.response().end(result.toBuffer());

            return null;
        });
    }
}