package com.kronostools.timehammer.chatbot.processor;

import com.kronostools.timehammer.chatbot.enums.ChatbotCommand;
import com.kronostools.timehammer.chatbot.utils.RoutesConstants;
import com.kronostools.timehammer.chatbot.utils.RoutesUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class TelegramCommandInterceptor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramCommandInterceptor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.debug("Checking if command is present -> {}", exchange.getIn().getBody());

        final IncomingMessage incomingMessage = exchange.getIn().getBody(IncomingMessage.class);

        Boolean commandPresent = RoutesUtils.hasCommand(incomingMessage.getText());
        exchange.getMessage().setHeader(RoutesConstants.Headers.COMMAND_PRESENT, commandPresent);

        if (commandPresent) {
            final Optional<ChatbotCommand> command = RoutesUtils.getCommand(incomingMessage.getText());
            exchange.getMessage().setHeader(RoutesConstants.Headers.COMMAND_RECOGNIZED, command.isPresent());

            command.ifPresent(c -> {
                exchange.getMessage().setHeader(RoutesConstants.Headers.COMMAND, c);
            });
        }
    }
}