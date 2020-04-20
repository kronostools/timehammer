package com.kronostools.timehammer.chatbot.routes;

import com.kronostools.timehammer.config.TimehammerConfig;
import org.apache.camel.builder.RouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TelegramChatbotNotifierRoute extends RouteBuilder {
    private final TimehammerConfig timehammerConfig;

    public TelegramChatbotNotifierRoute(final TimehammerConfig timehammerConfig) {
        this.timehammerConfig = timehammerConfig;
    }

    @Override
    public void configure() throws Exception {
        from("direct:notify")
                .toF("telegram:bots?authorizationToken=%s", timehammerConfig.getChatbot().getToken());
    }
}