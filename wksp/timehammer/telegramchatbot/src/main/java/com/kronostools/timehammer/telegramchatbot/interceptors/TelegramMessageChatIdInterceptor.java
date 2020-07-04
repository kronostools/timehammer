package com.kronostools.timehammer.telegramchatbot.interceptors;

import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotInputMessageBuilder;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.telegramchatbot.constants.RoutesConstants.Headers;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class TelegramMessageChatIdInterceptor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramMessageChatIdInterceptor.class);

    private final TimeMachineService timeMachineService;

    public TelegramMessageChatIdInterceptor(final TimeMachineService timeMachineService) {
        this.timeMachineService = timeMachineService;
    }

    @Override
    public void process(final Exchange exchange) {
        LOG.debug("Getting chatId and messageId from message ...");

        final IncomingMessage incomingMessage = exchange.getIn().getBody(IncomingMessage.class);

        Optional.ofNullable(incomingMessage).ifPresent(im -> {
            final LocalDateTime generated = timeMachineService.isMocked() ? timeMachineService.getNow() : CommonDateTimeUtils.getDateTime(im.getDate());

            exchange.getMessage().setHeader(Headers.COMMAND_MESSAGE, new TelegramChatbotInputMessageBuilder()
                    .generated(generated)
                    .chatId(im.getChat().getId())
                    .messageId(im.getMessageId())
                    .text(im.getText())
                    .build());
        });
    }
}