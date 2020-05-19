package com.kronostools.timehammer.telegramchatbotnotifier.routes;

import com.kronostools.timehammer.telegramchatbotnotifier.config.ChatbotConfig;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TelegramChatbotNotifierRoute extends RouteBuilder {
    private final ChatbotConfig chatbotConfig;

    public TelegramChatbotNotifierRoute(final ChatbotConfig chatbotConfig) {
        this.chatbotConfig = chatbotConfig;
    }

    @Override
    public void configure() throws Exception {
        from("direct:notify")
                .toF("telegram:bots?authorizationToken=%s", chatbotConfig.getToken());
    }
}