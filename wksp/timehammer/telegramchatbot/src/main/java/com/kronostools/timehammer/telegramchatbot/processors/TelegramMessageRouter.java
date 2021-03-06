package com.kronostools.timehammer.telegramchatbot.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotInputMessage;
import com.kronostools.timehammer.telegramchatbot.utils.RoutesUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TelegramMessageRouter implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramMessageRouter.class);

    private final Emitter<TelegramChatbotInputMessage> messageChannel;

    public TelegramMessageRouter(@Channel(Channels.COMMAND_PREPROCESS) final Emitter<TelegramChatbotInputMessage> messageChannel) {
        this.messageChannel = messageChannel;
    }

    @Override
    public void process(final Exchange exchange) {
        messageChannel.send(RoutesUtils.getCommandMessage(exchange)).handle((Void, e) -> {
            if (e != null) {
                LOG.error("Exception while routing chatbot message", e);
            } else {
                LOG.debug("Routed chatbot message");
            }

            return null;
        });
    }
}