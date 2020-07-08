package com.kronostools.timehammer.telegramchatbot.client;

import com.kronostools.timehammer.telegramchatbot.model.SetMyCommandsRequest;
import io.smallrye.mutiny.Uni;

public interface TelegramClient {
    Uni<String> getMe();

    Uni<Void> setMyCommands(SetMyCommandsRequest setMyCommandsRequest);
}