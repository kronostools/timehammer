package com.kronostools.timehammer.telegramchatbotnotifier.client;

import io.smallrye.mutiny.Uni;

public interface TelegramClient {
    Uni<String> getMe();
}