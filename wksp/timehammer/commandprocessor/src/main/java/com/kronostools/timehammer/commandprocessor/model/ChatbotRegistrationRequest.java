package com.kronostools.timehammer.commandprocessor.model;

import java.time.LocalDateTime;

public class ChatbotRegistrationRequest {
    private final String workerInternalId;
    private final String chatId;
    private final LocalDateTime requested;

    public ChatbotRegistrationRequest(final String workerInternalId, final String chatId, final LocalDateTime requested) {
        this.workerInternalId = workerInternalId;
        this.chatId = chatId;
        this.requested = requested;
    }

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public String getChatId() {
        return chatId;
    }

    public LocalDateTime getRequested() {
        return requested;
    }
}