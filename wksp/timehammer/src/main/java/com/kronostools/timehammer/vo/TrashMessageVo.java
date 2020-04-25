package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.utils.Utils;

import java.time.LocalDateTime;

public class TrashMessageVo {
    private final String chatId;
    private final LocalDateTime timestamp;
    private final String text;

    public TrashMessageVo(final String chatId, final LocalDateTime timestamp, final String text) {
        this.chatId = chatId;
        this.timestamp = timestamp;
        this.text = text;
    }

    public String getChatId() {
        return chatId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TrashMessageVo{" +
                "chatId='" + chatId + '\'' +
                ", timestamp=" + TimeMachineService.formatDateTimeFull(timestamp) +
                ", message='" + Utils.truncateString(text) + '\'' +
                '}';
    }
}