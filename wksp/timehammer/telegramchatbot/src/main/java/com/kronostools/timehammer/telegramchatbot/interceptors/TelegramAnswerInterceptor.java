package com.kronostools.timehammer.telegramchatbot.interceptors;

import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessageBuilder;
import com.kronostools.timehammer.telegramchatbot.constants.RoutesConstants;
import com.kronostools.timehammer.telegramchatbot.utils.RoutesUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.IncomingCallbackQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TelegramAnswerInterceptor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramAnswerInterceptor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.debug("Getting answer ...");

        final IncomingCallbackQuery incomingCallbackQuery = exchange.getIn().getBody(IncomingCallbackQuery.class);

        final String rawAnswer = incomingCallbackQuery.getData();

        LOG.debug("Raw answer is: {}", rawAnswer);

        exchange.getMessage()
                .setHeader(RoutesConstants.Headers.ANSWER_MESSAGE, TelegramChatbotAnswerMessageBuilder
                        .copy(RoutesUtils.getAnswerMessage(exchange))
                        .rawAnswer(rawAnswer)
                        .build());
    }
}