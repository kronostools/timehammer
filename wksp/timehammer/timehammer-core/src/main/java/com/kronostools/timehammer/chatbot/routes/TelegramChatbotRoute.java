package com.kronostools.timehammer.chatbot.routes;

import com.kronostools.timehammer.chatbot.processor.*;
import com.kronostools.timehammer.config.TimehammerConfig;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.model.IncomingCallbackQuery;
import org.apache.camel.component.telegram.model.IncomingMessage;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TelegramChatbotRoute extends RouteBuilder {
    private final TimehammerConfig timehammerConfig;
    private final TelegramChatIdInterceptor telegramChatIdInterceptor;
    private final TelegramCommandInterceptor telegramCommandInterceptor;
    private final TelegramAnswerInterceptor telegramAnswerInterceptor;
    private final TelegramMessageProcessor telegramMessageProcessor;
    private final TelegramAnswerProcessor telegramAnswerProcessor;

    public TelegramChatbotRoute(final TimehammerConfig timehammerConfig,
                                final TelegramChatIdInterceptor telegramChatIdInterceptor,
                                final TelegramCommandInterceptor telegramCommandInterceptor,
                                final TelegramAnswerInterceptor telegramAnswerInterceptor,
                                final TelegramMessageProcessor telegramMessageProcessor,
                                final TelegramAnswerProcessor telegramAnswerProcessor) {
        this.timehammerConfig = timehammerConfig;
        this.telegramChatIdInterceptor = telegramChatIdInterceptor;
        this.telegramCommandInterceptor = telegramCommandInterceptor;
        this.telegramAnswerInterceptor = telegramAnswerInterceptor;
        this.telegramMessageProcessor = telegramMessageProcessor;
        this.telegramAnswerProcessor = telegramAnswerProcessor;
    }

    @Override
    public void configure() throws Exception {
        fromF("telegram:bots?authorizationToken=%s", timehammerConfig.getChatbot().getToken())
                .process(telegramChatIdInterceptor)
                .choice()
                    .when(body().isInstanceOf(IncomingMessage.class))
                        .process(telegramCommandInterceptor)
                        .process(telegramMessageProcessor)
                    .when(body().isInstanceOf(IncomingCallbackQuery.class))
                        .process(telegramAnswerInterceptor)
                        .process(telegramAnswerProcessor)
                .end()
                .to("direct:notify");
    }
}