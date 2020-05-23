package com.kronostools.timehammer.telegramchatbotnotifier.service;

import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessageBuilder;
import com.kronostools.timehammer.common.utils.CommonUtils;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.telegram.TelegramParseMode;
import org.apache.camel.component.telegram.model.OutgoingMessage;
import org.apache.camel.component.telegram.model.OutgoingTextMessage;
import org.apache.camel.component.telegram.model.ReplyMarkup;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public abstract class AbstractNotificationService implements NotificationService {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected final CamelContext camelContext;

    AbstractNotificationService(final CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public CompletionStage<Void> notify(final Message<TelegramChatbotNotificationMessage> message) {
        final TelegramChatbotNotificationMessage telegramChatbotNotificationMessage = TelegramChatbotNotificationMessageBuilder.copy(message.getPayload()).build();

        final OutgoingMessage outgoingMessage = getOutgoingMessage(telegramChatbotNotificationMessage);

        final ProducerTemplate producerTemplate = camelContext.createProducerTemplate();

        return producerTemplate.asyncSendBody("direct:notify", outgoingMessage).minimalCompletionStage().handle((Void, e) -> {
            if (e == null) {
                message.ack();
            } else {
                LOG.warn("There was an error notifying someone");
            }

            return null;
        });
    }

    private OutgoingMessage getOutgoingMessage(final TelegramChatbotNotificationMessage telegramChatbotNotificationMessage) {
        return getOutgoingMessage(telegramChatbotNotificationMessage.getChatId(), telegramChatbotNotificationMessage.getText(), null);
    }

    private OutgoingMessage getOutgoingMessage(final String chatId, final String text, final ReplyMarkup replyMarkup) {
        OutgoingMessage message = new OutgoingTextMessage.Builder()
                .text(isDemoMode() ? CommonUtils.stringFormat("*(demo mode)*\n{}", text) : text)
                .parseMode(TelegramParseMode.MARKDOWN.getCode())
                .replyMarkup(replyMarkup)
                .build();

        message.setChatId(chatId);

        return message;
    }

    protected abstract boolean isDemoMode();
}