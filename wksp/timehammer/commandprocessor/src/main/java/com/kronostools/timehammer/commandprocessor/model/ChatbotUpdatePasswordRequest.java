package com.kronostools.timehammer.commandprocessor.model;

import com.kronostools.timehammer.common.constants.Company;

import java.time.LocalDateTime;

public class ChatbotUpdatePasswordRequest {
    private final String workerInternalId;
    private final String chatId;
    private final LocalDateTime requested;
    private final Company company;
    private final String workerExternalId;

    public ChatbotUpdatePasswordRequest(final String workerInternalId, final String chatId, final LocalDateTime requested, final Company company, final String workerExternalId) {
        this.workerInternalId = workerInternalId;
        this.chatId = chatId;
        this.requested = requested;
        this.company = company;
        this.workerExternalId = workerExternalId;
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

    public Company getCompany() {
        return company;
    }

    public String getWorkerExternalId() {
        return workerExternalId;
    }
}