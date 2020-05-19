package com.kronostools.timehammer.common.messages.telegramchatbot.deserializers;

import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessage;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class TelegramChatbotNotificationMessageJacksonDeserializer extends ObjectMapperDeserializer<TelegramChatbotNotificationMessage> {
    public TelegramChatbotNotificationMessageJacksonDeserializer() {
        super(TelegramChatbotNotificationMessage.class);
    }
}