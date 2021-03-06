package com.kronostools.timehammer.telegramchatbot.interceptors;

import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessageBuilder;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.telegramchatbot.constants.RoutesConstants.Headers;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.IncomingCallbackQuery;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class TelegramAnswerChatIdInterceptor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramAnswerChatIdInterceptor.class);

    private final TimeMachineService timeMachineService;

    public TelegramAnswerChatIdInterceptor(final TimeMachineService timeMachineService) {
        this.timeMachineService = timeMachineService;
    }

    @Override
    public void process(final Exchange exchange) {
        LOG.debug("Getting chatId and messageId from answer message ...");

        final IncomingMessage incomingMessage = exchange.getIn().getBody(IncomingCallbackQuery.class).getMessage();

        Optional.ofNullable(incomingMessage).ifPresent(im -> {
            final LocalDateTime generated = timeMachineService.isMocked() ? timeMachineService.getNow() : CommonDateTimeUtils.getDateTime(im.getDate());
            final String chatId = incomingMessage.getChat().getId();
            final Long messageId = incomingMessage.getMessageId();

            exchange.getMessage().setHeader(Headers.ANSWER_MESSAGE, new TelegramChatbotAnswerMessageBuilder()
                    .generated(generated)
                    .chatId(chatId)
                    .messageId(messageId).build());
        });
    }
}