package com.kronostools.timehammer.web.resource;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.catalog.CatalogMessage;
import com.kronostools.timehammer.common.messages.catalog.CatalogMessageBuilder;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.web.dto.CatalogRequestDto;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@RouteBase(path = "/catalog")
public class CatalogRoutes {
    private final TimeMachineService timeMachineService;
    private final Emitter<CatalogMessage> catalogMessageChannel;

    public CatalogRoutes(final TimeMachineService timeMachineService,
                         @Channel(Channels.CATALOG_REQUEST) final Emitter<CatalogMessage> catalogMessageChannel) {
        this.timeMachineService = timeMachineService;
        this.catalogMessageChannel = catalogMessageChannel;
    }

    @Route(path = "/request", methods = HttpMethod.POST)
    void catalogRequest(RoutingContext rc) {
        final CatalogRequestDto catalogRequest = rc.getBodyAsJson().mapTo(CatalogRequestDto.class);

        final CatalogMessage timeMachineEvent = new CatalogMessageBuilder()
                .generated(timeMachineService.getNow())
                .requesterId(catalogRequest.getRequesterId())
                .requestedCatalogs(catalogRequest.getRequestedCatalogs())
                .build();

        final HttpServerRequest request = rc.request();

        request.pause();

        catalogMessageChannel.send(timeMachineEvent).handleAsync((Void, e) -> {
            request.resume();

            final JsonObject result = new JsonObject();
            result.put("result", e == null);

            rc.response().end(result.toBuffer());

            return null;
        });
    }
}