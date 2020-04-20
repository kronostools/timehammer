package com.kronostools.timehammer.vo;

public class WorkerChatVo {
    private final String chatId;

    public WorkerChatVo(final String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    @Override
    public String toString() {
        return "WorkerChatVo{" +
                "chatId=" + chatId +
                '}';
    }
}