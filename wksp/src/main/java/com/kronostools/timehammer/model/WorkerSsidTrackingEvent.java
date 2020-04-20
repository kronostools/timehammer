package com.kronostools.timehammer.model;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "WorkerSsidTrackingEvent")
@Table(name = "worker_ssid_tracking_event")
public class WorkerSsidTrackingEvent {
    @EmbeddedId
    private WorkerSsidTrackingEventId id;

    @MapsId("workerExtenalId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Worker worker;

    public WorkerSsidTrackingEventId getId() {
        return id;
    }

    public void setId(WorkerSsidTrackingEventId id) {
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
        WorkerSsidTrackingEvent that = (WorkerSsidTrackingEvent) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
