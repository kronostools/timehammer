package com.kronostools.timehammer.telegramchatbot.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessage;
import com.kronostools.timehammer.telegramchatbot.utils.RoutesUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TelegramAnswerRouter implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramAnswerRouter.class);

    private final Emitter<TelegramChatbotAnswerMessage> answerChannel;

    public TelegramAnswerRouter(@Channel(Channels.ANSWER_ID) final Emitter<TelegramChatbotAnswerMessage> answerChannel) {
        this.answerChannel = answerChannel;
    }

    @Override
    public void process(final Exchange exchange) {
        answerChannel.send(RoutesUtils.getAnswerMessage(exchange)).handle((Void, e) -> {
            if (e != null) {
                LOG.error("Exception while routing chatbot answer", e);
            } else {
                LOG.debug("Routed chatbot answer");
            }

            return null;
        });
    }
}