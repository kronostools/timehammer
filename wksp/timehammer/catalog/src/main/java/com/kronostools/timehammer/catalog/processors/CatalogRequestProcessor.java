package com.kronostools.timehammer.catalog.processors;

import com.kronostools.timehammer.catalog.service.CatalogService;
import com.kronostools.timehammer.common.constants.CatalogType;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.catalog.CatalogMessage;
import com.kronostools.timehammer.common.messages.catalog.CatalogMessageBuilder;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

import static com.kronostools.timehammer.common.utils.CommonUtils.stringFormat;

@ApplicationScoped
public class CatalogRequestProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(CatalogRequestProcessor.class);

    private final Emitter<CatalogMessage> catalogRerequestChannel;
    private final Emitter<CatalogMessage> catalogResponseChannel;
    private final CatalogService catalogService;

    public CatalogRequestProcessor(@Channel(Channels.CATALOG_REREQUEST) final Emitter<CatalogMessage> catalogRerequestChannel,
                                   @Channel(Channels.CATALOG_RESPONSE) final Emitter<CatalogMessage> catalogResponseChannel,
                                   final CatalogService catalogService) {
        this.catalogRerequestChannel = catalogResponseChannel;
        this.catalogResponseChannel = catalogRerequestChannel;
        this.catalogService = catalogService;
    }

    @Incoming(Channels.CATALOG_REQUEST)
    public CompletionStage<Void> process(final Message<CatalogMessage> message) {
        final CatalogMessage catalogMessage = CatalogMessageBuilder.copy(message.getPayload()).build();

        final CatalogType catalogType = catalogMessage.getRequestedCatalogs().pop();

        LOG.info("Getting values of catalog '{}' requested by '{}' ...", catalogType, catalogMessage.getRequesterId());

        final CatalogMessage nextCatalogMessage = catalogService.getCatalog(catalogType)
                .map(crp -> CatalogMessageBuilder
                        .copy(catalogMessage)
                        .putCatalogResponse(catalogType, crp)
                        .build())
                .await().indefinitely();

        if (catalogMessage.getRequestedCatalogs().isEmpty()) {
            LOG.info("Catalog '{}' was the last one requested by '{}' -> routing response to: {} ...", catalogType, catalogMessage.getRequesterId(), Channels.CATALOG_RESPONSE);

            return catalogResponseChannel.send(nextCatalogMessage)
                    .handle(getMessageHandler(message, catalogMessage.getRequesterId()));
        } else {
            LOG.info("Catalog '{}' was not the last one requested by '{}', {} catalogs left to get -> routing response to: {} ...", catalogType, catalogMessage.getRequesterId(), catalogMessage.getRequestedCatalogs().size(), Channels.CATALOG_REREQUEST);

            return catalogRerequestChannel.send(nextCatalogMessage)
                    .handle(getMessageHandler(message, catalogMessage.getRequesterId()));
        }
    }

    private BiFunction<? super Void, Throwable, Void> getMessageHandler(final Message<?> message, final String requesterId) {
        return (Void, e) -> {
            if (e != null) {
                LOG.error(stringFormat("Exception while routing catalog message requested by '{}'", requesterId), e);
            } else {
                message.ack();
                LOG.debug("Catalog message requested by '{}' routed successfully", requesterId);
            }

            return null;
        };
    }
}