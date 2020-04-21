package com.kronostools.timehammer.model;

import com.kronostools.timehammer.enums.SsidTrackingEventType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class WorkerSsidTrackingEventId implements Serializable {
    @Column(name = "worker_external_id", nullable = false, updatable = false)
    private String workerExternalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private SsidTrackingEventType eventType;

    @Column(nullable = false)
    private LocalDateTime occurred;

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public void setWorkerExternalId(String workerExtenalId) {
        this.workerExternalId = workerExtenalId;
    }

    public SsidTrackingEventType getEventType() {
        return eventType;
    }

    public void setEventType(SsidTrackingEventType eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getOccurred() {
        return occurred;
    }

    public void setOccurred(LocalDateTime occurred) {
        this.occurred = occurred;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerSsidTrackingEventId that = (WorkerSsidTrackingEventId) o;
        return workerExternalId.equals(that.workerExternalId) &&
                eventType == that.eventType &&
                occurred.equals(that.occurred);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerExternalId, eventType, occurred);
    }
}
