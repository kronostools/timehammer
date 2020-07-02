package com.kronostools.timehammer.telegramchatbot.routes;

import com.kronostools.timehammer.telegramchatbot.config.ChatbotConfig;
import com.kronostools.timehammer.telegramchatbot.interceptors.TelegramAnswerChatIdInterceptor;
import com.kronostools.timehammer.telegramchatbot.interceptors.TelegramAnswerInterceptor;
import com.kronostools.timehammer.telegramchatbot.interceptors.TelegramMessageChatIdInterceptor;
import com.kronostools.timehammer.telegramchatbot.interceptors.TelegramMessageCommandInterceptor;
import com.kronostools.timehammer.telegramchatbot.processors.TelegramAnswerRouter;
import com.kronostools.timehammer.telegramchatbot.processors.TelegramMessageRouter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.model.IncomingCallbackQuery;
import org.apache.camel.component.telegram.model.IncomingMessage;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TelegramChatbotRoute extends RouteBuilder {
    private final ChatbotConfig chatbotConfig;
    private final TelegramMessageChatIdInterceptor telegramMessageChatIdInterceptor;
    private final TelegramMessageCommandInterceptor telegramMessageCommandInterceptor;
    private final TelegramMessageRouter telegramMessageRouter;

    private final TelegramAnswerChatIdInterceptor telegramAnswerChatIdInterceptor;
    private final TelegramAnswerInterceptor telegramAnswerInterceptor;
    private final TelegramAnswerRouter telegramAnswerRouter;

    public TelegramChatbotRoute(final ChatbotConfig chatbotConfig,
                                final TelegramMessageChatIdInterceptor telegramMessageChatIdInterceptor,
                                final TelegramMessageCommandInterceptor telegramMessageCommandInterceptor,
                                final TelegramMessageRouter telegramMessageRouter,
                                final TelegramAnswerChatIdInterceptor telegramAnswerChatIdInterceptor,
                                final TelegramAnswerInterceptor telegramAnswerInterceptor,
                                final TelegramAnswerRouter telegramAnswerRouter
                                ) {
        this.chatbotConfig = chatbotConfig;
        this.telegramMessageChatIdInterceptor = telegramMessageChatIdInterceptor;
        this.telegramMessageCommandInterceptor = telegramMessageCommandInterceptor;
        this.telegramMessageRouter = telegramMessageRouter;
        this.telegramAnswerChatIdInterceptor = telegramAnswerChatIdInterceptor;
        this.telegramAnswerInterceptor = telegramAnswerInterceptor;
        this.telegramAnswerRouter = telegramAnswerRouter;
    }

    @Override
    public void configure() throws Exception {
        fromF("telegram:bots?authorizationToken=%s", chatbotConfig.getToken())
                .choice()
                    .when(body().isInstanceOf(IncomingMessage.class))
                        .process(telegramMessageChatIdInterceptor)
                        .process(telegramMessageCommandInterceptor)
                        .process(telegramMessageRouter)
                    .when(body().isInstanceOf(IncomingCallbackQuery.class))
                        .process(telegramAnswerChatIdInterceptor)
                        .process(telegramAnswerInterceptor)
                        .process(telegramAnswerRouter)
                .end();
    }
}