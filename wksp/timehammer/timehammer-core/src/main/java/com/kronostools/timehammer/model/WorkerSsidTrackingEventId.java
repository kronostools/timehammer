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
    @Column(name = "worker_internal_id", nullable = false, updatable = false)
    private String workerInternalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private SsidTrackingEventType eventType;

    @Column(nullable = false)
    private LocalDateTime occurred;

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public void setWorkerInternalId(String workerInternalId) {
        this.workerInternalId = workerInternalId;
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
        return workerInternalId.equals(that.workerInternalId) &&
                eventType == that.eventType &&
                occurred.equals(that.occurred);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerInternalId, eventType, occurred);
    }
}
