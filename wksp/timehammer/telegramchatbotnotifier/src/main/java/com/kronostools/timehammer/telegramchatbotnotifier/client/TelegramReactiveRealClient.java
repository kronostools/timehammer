package com.kronostools.timehammer.telegramchatbotnotifier.client;

import com.kronostools.timehammer.common.utils.CommonUtils;
import com.kronostools.timehammer.telegramchatbotnotifier.config.ChatbotConfig;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

import javax.ws.rs.core.MediaType;

public class TelegramReactiveRealClient implements TelegramClient {

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

    private String getUrl(final String urlPart) {
        return CommonUtils.stringFormat("{}/{}", baseUrl, urlPart);
    }
}