package com.kronostools.timehammer.common.messages.telegramchatbot.deserializers;

import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotInputMessage;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class TelegramChatbotInputMessageJacksonDeserializer extends ObjectMapperDeserializer<TelegramChatbotInputMessage> {
    public TelegramChatbotInputMessageJacksonDeserializer() {
        super(TelegramChatbotInputMessage.class);
    }
}