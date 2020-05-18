package com.kronostools.timehammer.chatbot.routes;

import com.kronostools.timehammer.chatbot.config.ChatbotConfig;
import com.kronostools.timehammer.chatbot.interceptors.TelegramAnswerChatIdInterceptor;
import com.kronostools.timehammer.chatbot.interceptors.TelegramMessageChatIdInterceptor;
import com.kronostools.timehammer.chatbot.interceptors.TelegramMessageCommandInterceptor;
import com.kronostools.timehammer.chatbot.processors.TelegramChatbotMessageRouter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.model.IncomingCallbackQuery;
import org.apache.camel.component.telegram.model.IncomingMessage;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TelegramChatbotRoute extends RouteBuilder {
    private final ChatbotConfig chatbotConfig;
    private final TelegramMessageChatIdInterceptor telegramMessageChatIdInterceptor;
    private final TelegramMessageCommandInterceptor telegramMessageCommandInterceptor;
    private final TelegramChatbotMessageRouter telegramChatbotMessageRouter;
    private final TelegramAnswerChatIdInterceptor telegramAnswerChatIdInterceptor;
    // TODO: uncomment and implement
    //private final TelegramAnswerInterceptor telegramAnswerInterceptor;
    //private final TelegramAnswerProcessor telegramAnswerProcessor;

    public TelegramChatbotRoute(final ChatbotConfig chatbotConfig,
                                final TelegramMessageChatIdInterceptor telegramMessageChatIdInterceptor,
                                final TelegramMessageCommandInterceptor telegramMessageCommandInterceptor,
                                final TelegramChatbotMessageRouter telegramChatbotMessageRouter,
                                final TelegramAnswerChatIdInterceptor telegramAnswerChatIdInterceptor
                                // TODO: uncomment ant implement
                                //final TelegramAnswerInterceptor telegramAnswerInterceptor,
                                //final TelegramAnswerProcessor telegramAnswerProcessor
                                ) {
        this.chatbotConfig = chatbotConfig;
        this.telegramMessageChatIdInterceptor = telegramMessageChatIdInterceptor;
        this.telegramMessageCommandInterceptor = telegramMessageCommandInterceptor;
        this.telegramChatbotMessageRouter = telegramChatbotMessageRouter;
        this.telegramAnswerChatIdInterceptor = telegramAnswerChatIdInterceptor;
        // TODO: uncomment and implement
        //this.telegramAnswerInterceptor = telegramAnswerInterceptor;
        //this.telegramAnswerProcessor = telegramAnswerProcessor;
    }

    @Override
    public void configure() throws Exception {
        fromF("telegram:bots?authorizationToken=%s", chatbotConfig.getToken())
                .choice()
                    .when(body().isInstanceOf(IncomingMessage.class))
                        .process(telegramMessageChatIdInterceptor)
                        .process(telegramMessageCommandInterceptor)
                        .process(telegramChatbotMessageRouter)
                    .when(body().isInstanceOf(IncomingCallbackQuery.class))
                        .process(telegramAnswerChatIdInterceptor)
                        // TODO: uncomment and implement
                        //.process(telegramAnswerInterceptor)
                        //.process(telegramAnswerProcessor)
                .end();
    }
}