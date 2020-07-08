package com.kronostools.timehammer.telegramchatbot.client;

import com.kronostools.timehammer.common.utils.CommonUtils;
import com.kronostools.timehammer.telegramchatbot.config.ChatbotConfig;
import com.kronostools.timehammer.telegramchatbot.model.SetMyCommandsRequest;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
public class TelegramReactiveRealClient implements TelegramClient {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramReactiveRealClient.class);

    private final WebClient client;
    private final String baseUrl;

    public TelegramReactiveRealClient(final Vertx vertx, final ChatbotConfig chatbotConfig) {
        this.client = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost("api.telegram.org")
                .setDefaultPort(443)
                .setSsl(true)
                .setTrustAll(true));

        this.baseUrl = CommonUtils.stringFormat("/bot{}", chatbotConfig.getToken());
    }

    @Override
    public Uni<String> getMe() {
        return client.get(getUrl("getMe"))
                .putHeader("Accept", MediaType.APPLICATION_JSON)
                .send()
                .map(response -> response.statusCode() == 200 ? response.bodyAsString() : null)
                .onFailure()
                    .recoverWithItem(() -> null);
    }

    @Override
    public Uni<Void> setMyCommands(SetMyCommandsRequest setMyCommandsRequest) {
        return client.post(getUrl("setMyCommands"))
                .putHeader("Accept", MediaType.APPLICATION_JSON)
                .putHeader("Content-Type", MediaType.APPLICATION_JSON)
                .sendBuffer(setMyCommandsRequest.toBuffer())
                .onItem().produceUni(response -> {
                    if (response.statusCode() == 200) {
                        LOG.info("Chatbot commands published successfully!");
                    } else {
                        LOG.warn("Chatbot commands could not be published. Response status code: {}. Response body: {}", response.statusCode(), response.bodyAsString());
                    }

                    return Uni.createFrom().voidItem();
                })
                .onFailure()
                    .recoverWithItem(e -> {
                        LOG.warn("Chatbot commands could not be published because an unexpected error occurred. Error: {}", e.getMessage());

                        return null;
                    });
    }

    private String getUrl(final String urlPart) {
        return CommonUtils.stringFormat("{}/{}", baseUrl, urlPart);
    }
}