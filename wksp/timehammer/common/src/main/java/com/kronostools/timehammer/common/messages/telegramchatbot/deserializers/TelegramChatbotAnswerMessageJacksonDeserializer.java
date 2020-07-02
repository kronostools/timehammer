package com.kronostools.timehammer.common.messages.telegramchatbot.deserializers;

import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessage;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class TelegramChatbotAnswerMessageJacksonDeserializer extends ObjectMapperDeserializer<TelegramChatbotAnswerMessage> {
    public TelegramChatbotAnswerMessageJacksonDeserializer() {
        super(TelegramChatbotAnswerMessage.class);
    }
}