package com.kronostools.timehammer.model;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "WorkerChat")
@Table(name = "worker_chat")
public class WorkerChat {
    @EmbeddedId
    private WorkerChatId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_internal_id", insertable = false, updatable = false)
    private Worker worker;

    public WorkerChatId getId() {
        return id;
    }

    public void setId(WorkerChatId id) {
        this.id = id;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerChat that = (WorkerChat) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}