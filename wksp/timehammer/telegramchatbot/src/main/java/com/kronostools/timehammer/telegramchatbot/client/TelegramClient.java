package com.kronostools.timehammer.telegramchatbot.client;

import io.smallrye.mutiny.Uni;

public interface TelegramClient {
    Uni<String> getMe();
}