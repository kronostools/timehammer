package com.kronostools.timehammer.telegramchatbotnotifier.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessageBuilder;
import com.kronostools.timehammer.common.messages.telegramchatbot.model.KeyboardOption;
import com.kronostools.timehammer.common.utils.CommonUtils;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.telegram.TelegramParseMode;
import org.apache.camel.component.telegram.model.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@ApplicationScoped
public class DefaultNotificationService implements NotificationService {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected final CamelContext camelContext;
    protected final ProducerTemplate producerTemplate;

    private final Cache<String, LinkedList<Long>> messageWithKeyboard;

    @ConfigProperty(name = "timehammer.mocks.notificationservice")
    Boolean notificationServiceMocked;

    public DefaultNotificationService(final CamelContext camelContext) {
        this.camelContext = camelContext;
        this.producerTemplate = camelContext.createProducerTemplate();

        this.messageWithKeyboard = Caffeine.newBuilder().build();
    }

    @PreDestroy
    public void destroy() {
        try {
            producerTemplate.close();
        } catch (IOException e) {
            LOG.error("An unexpected error occurred while closing camel producer template. Error: {}", e.getMessage());
        }
    }

    @Override
    public CompletionStage<Void> notify(final Message<TelegramChatbotNotificationMessage> message) {
        final TelegramChatbotNotificationMessage notificationMessage = TelegramChatbotNotificationMessageBuilder.copy(message.getPayload()).build();

        final OutgoingMessage outgoingMessage = getOutgoingMessage(notificationMessage);

        LOG.info("Notifying to chat '{}' ...", notificationMessage.getChatId());

        if (LOG.isDebugEnabled()) {
            try {
                LOG.debug("Message to notify: '{}'", new ObjectMapper().writeValueAsString(outgoingMessage));
            } catch (JsonProcessingException e) {
                LOG.debug("Error converting message to notify to JSON");
            }
        }

        removePreviousKeyboard(notificationMessage);

        if (notificationMessage.hasKeyboard()) {
            LOG.debug("Notifying message with keyboard to chat '{}'", notificationMessage.getChatId());

            return notifyAsync(outgoingMessage).minimalCompletionStage().handle((r, e) -> {
                if (e == null) {
                    if (r instanceof MessageResult) {
                        final Long messageId = ((MessageResult) r).getMessage().getMessageId();

                        LOG.debug("Chat '{}' was notified successfully with keyboard in message '{}'", notificationMessage.getChatId(), messageId);

                        final LinkedList<Long> messageIds = messageWithKeyboard.getIfPresent(notificationMessage.getChatId());

                        if (messageIds == null) {
                            messageWithKeyboard.put(notificationMessage.getChatId(), new LinkedList<>() {{
                                push(messageId);
                            }});
                        } else {
                            messageIds.push(messageId);
                        }
                    } else {
                        LOG.warn("Chat '{}' was notified successfully with keyboard, but the id of message was not stored, so it will not be able to remove this keyboard in future", notificationMessage.getChatId());
                    }

                    message.ack();
                } else {
                    LOG.warn("There was an error notifying chat '{}' with keyboard", notificationMessage.getChatId());
                }

                return null;
            });
        } else {
            LOG.debug("Notifying message without keyboard to chat '{}'", notificationMessage.getChatId());

            return notifyAsync(outgoingMessage).minimalCompletionStage().handle((r, e) -> {
                if (e == null) {
                    message.ack();
                } else {
                    LOG.warn("There was an error notifying chat '{}'", notificationMessage.getChatId());
                }

                return null;
            });
        }
    }

    private void removePreviousKeyboard(final TelegramChatbotNotificationMessage notificationMessage) {
        if (notificationMessage.isClearPreviousKeyboard()) {
            final LinkedList<Long> messageIds = messageWithKeyboard.getIfPresent(notificationMessage.getChatId());

            if (messageIds != null) {
                if (!messageIds.isEmpty()) {
                    LOG.debug("Removing keyboards from {} previous messages in chat '{}' ...", messageIds.size(), notificationMessage.getChatId());
                }

                while (!messageIds.isEmpty()) {
                    final Long messageId = messageIds.pop();

                    LOG.debug("Message to chat '{}' has keyboard and there is a previous message ({}) with keyboard, removing it ...", notificationMessage.getChatId(), messageId);

                    final OutgoingMessage outgoingMessage = getOutgoingMessageToRemoveInlineKeyboard(notificationMessage.getChatId(), messageId);

                    try {
                        notify(outgoingMessage);
                        LOG.debug("Previous keyboard was removed successfully from message '{}' in chat '{}'", messageId, notificationMessage.getChatId());
                    } catch (Exception e) {
                        LOG.warn("Previous keyboard of message '{}' in chat '{}' could not be remove because an unexpected error. Error: {}", messageId, notificationMessage.getChatId(), e.getMessage());
                        messageIds.push(messageId);
                    }
                }
            } else {
                LOG.debug("There is no previous message with keyboard in chat '{}'", notificationMessage.getChatId());
            }
        }
    }

    public void notify(final OutgoingMessage message) {
        producerTemplate.sendBody("direct:notify", message);
    }

    public CompletableFuture<Object> notifyAsync(final OutgoingMessage message) {
        return producerTemplate.asyncSendBody("direct:notify", message);
    }

    private OutgoingMessage getOutgoingMessageToRemoveInlineKeyboard(final String chatId, final Long messageId) {
        final InlineKeyboardMarkup replyMarkup = InlineKeyboardMarkup
                .builder()
                .build();

        return EditMessageReplyMarkupMessage
                .builder()
                .chatId(chatId)
                .messageId(messageId.intValue())
                .replyMarkup(replyMarkup)
                .build();
    }

    private OutgoingMessage getOutgoingMessage(final TelegramChatbotNotificationMessage telegramChatbotNotificationMessage) {
        return getOutgoingMessage(telegramChatbotNotificationMessage.getChatId(), telegramChatbotNotificationMessage.getText(), getReplyMarkup(telegramChatbotNotificationMessage.getKeyboard()));
    }

    private OutgoingMessage getOutgoingMessage(final String chatId, final String text, final ReplyMarkup replyMarkup) {
        final String escapedText = escapeMarkdown(text);

        OutgoingMessage message = new OutgoingTextMessage.Builder()
                .text(isDemoMode() ? CommonUtils.stringFormat("*(demo mode)*\n{}", escapedText) : escapedText)
                .parseMode(TelegramParseMode.MARKDOWN.getCode())
                .replyMarkup(replyMarkup)
                .build();

        message.setChatId(chatId);

        return message;
    }

    private String escapeMarkdown(final String originalText) {
        return originalText.replaceAll("_", "\\\\_");
    }

    private ReplyMarkup getReplyMarkup(final List<KeyboardOption> keyboard) {
        final List<InlineKeyboardButton> buttons = Optional.ofNullable(keyboard).stream().flatMap(Collection::stream)
                .map(ko -> InlineKeyboardButton.builder()
                        .text(ko.getText())
                        .callbackData(ko.getCode())
                        .build())
                .collect(Collectors.toList());

        if (buttons.isEmpty()) {
            return null;
        } else {
            return InlineKeyboardMarkup.builder()
                    .addRow(buttons)
                    .build();
        }
    }

    protected boolean isDemoMode() {
        return notificationServiceMocked;
    }
}