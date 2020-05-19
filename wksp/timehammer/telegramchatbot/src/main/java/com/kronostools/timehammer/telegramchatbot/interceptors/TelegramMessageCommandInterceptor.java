package com.kronostools.timehammer.telegramchatbot.interceptors;

import com.kronostools.timehammer.common.constants.ChatbotCommand;
import com.kronostools.timehammer.common.messages.chatbot.ChatbotMessageBuilder;
import com.kronostools.timehammer.telegramchatbot.constants.RoutesConstants;
import com.kronostools.timehammer.telegramchatbot.utils.RoutesUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class TelegramMessageCommandInterceptor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramMessageCommandInterceptor.class);

    @Override
    public void process(final Exchange exchange) {
        LOG.debug("Checking if command is present ...");

        final IncomingMessage incomingMessage = exchange.getIn().getBody(IncomingMessage.class);

        final String incomingMessageText = incomingMessage.getText();

        if (RoutesUtils.hasCommand(incomingMessageText)) {
            final Optional<ChatbotCommand> command = RoutesUtils.getCommand(incomingMessageText);

            command.ifPresentOrElse(
                    // known command
                    c -> exchange.getMessage()
                                .setHeader(RoutesConstants.Headers.COMMAND_MESSAGE, ChatbotMessageBuilder
                                        .copy(RoutesUtils.getMessage(exchange))
                                        .command(c).build()),
                    // unknown command
                    () -> {
                        final String rawCommand = RoutesUtils.getRawCommand(incomingMessageText).orElse(null);

                        exchange.getMessage()
                                .setHeader(RoutesConstants.Headers.COMMAND_MESSAGE, ChatbotMessageBuilder
                                        .copy(RoutesUtils.getMessage(exchange))
                                        .rawCommand(rawCommand).build());
                    });
        }
    }
}