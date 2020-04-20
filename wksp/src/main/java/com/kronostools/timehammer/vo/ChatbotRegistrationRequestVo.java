package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDateTime;

public class ChatbotRegistrationRequestVo {
    private final String registrationId;
    private final String chatId;
    private final LocalDateTime requested;

    public ChatbotRegistrationRequestVo(final String registrationId, final String chatId, final LocalDateTime requested) {
        this.registrationId = registrationId;
        this.chatId = chatId;
        this.requested = requested;
    }

    public String getRegistrationId() {
        return registrationId;
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
                "registrationId='" + registrationId + '\'' +
                ", chatId=" + chatId +
                ", requested=" + TimeMachineService.formatDateTimeFull(requested) +
                '}';
    }
}