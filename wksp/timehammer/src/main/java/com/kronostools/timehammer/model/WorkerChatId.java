package com.kronostools.timehammer.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WorkerChatId implements Serializable {
    @Column(name = "worker_internal_id", nullable = false)
    private String workerInternalId;

    @Column(name = "chat_id", nullable = false, unique = true)
    private String chatId;

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public void setWorkerInternalId(String workerInternalId) {
        this.workerInternalId = workerInternalId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerChatId that = (WorkerChatId) o;
        return workerInternalId.equals(that.workerInternalId) &&
                chatId.equals(that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerInternalId, chatId);
    }
}
