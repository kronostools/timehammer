package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDateTime;

public class ChatbotRegistrationRequestVo {
    private final String internalId;
    private final String chatId;
    private final LocalDateTime requested;

    public ChatbotRegistrationRequestVo(final String internalId, final String chatId, final LocalDateTime requested) {
        this.internalId = internalId;
        this.chatId = chatId;
        this.requested = requested;
    }

    public String getInternalId() {
        return internalId;
    }

    public String getChatId() {
        return chatId;
    }

    public LocalDateTime getRequested() {
        return requested;
    }

    @Override
    public String toString() {
        return "ChatbotRegistrationRequestVo{" +
                "internalId='" + internalId + '\'' +
                ", chatId=" + chatId +
                ", requested=" + TimeMachineService.formatDateTimeFull(requested) +
                '}';
    }
}