package com.kronostools.timehammer.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "WorkerSsidTrackingInfo")
@Table(name = "worker_ssid_tracking_info")
public class WorkerSsidTrackingInfo {
    @Id
    @Column(name = "worker_internal_id", nullable = false, unique = true, updatable = false)
    private String workerInternalId;

    @Column(name = "ssid_reported")
    private String ssidReported;

    @Column
    private LocalDateTime reported;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Worker worker;

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public void setWorkerInternalId(String workerInternalId) {
        this.workerInternalId = workerInternalId;
    }

    public String getSsidReported() {
        return ssidReported;
    }

    public void setSsidReported(String ssidReported) {
        this.ssidReported = ssidReported;
    }

    public LocalDateTime getReported() {
        return reported;
    }

    public void setReported(LocalDateTime reported) {
        this.reported = reported;
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
        WorkerSsidTrackingInfo that = (WorkerSsidTrackingInfo) o;
        return workerInternalId.equals(that.workerInternalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerInternalId);
    }
}