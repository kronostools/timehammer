package com.kronostools.timehammer.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WorkerChatId implements Serializable {
    @Column(name = "worker_external_id", nullable = false)
    private String workerExternalId;

    @Column(name = "chat_id", nullable = false, unique = true)
    private String chatId;

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public void setWorkerExternalId(String workerExternalId) {
        this.workerExternalId = workerExternalId;
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
        return workerExternalId.equals(that.workerExternalId) &&
                chatId.equals(that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerExternalId, chatId);
    }
}
