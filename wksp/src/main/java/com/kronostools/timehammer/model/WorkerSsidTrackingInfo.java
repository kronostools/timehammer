package com.kronostools.timehammer.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "WorkerSsidTrackingInfo")
@Table(name = "worker_ssid_tracking_info")
public class WorkerSsidTrackingInfo {
    @Id
    @Column(name = "worker_external_id", nullable = false, unique = true, updatable = false)
    private String workerExternalId;

    @Column(name = "ssid_reported")
    private String ssidReported;

    @Column
    private LocalDateTime reported;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Worker worker;

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public void setWorkerExternalId(String workerExternalId) {
        this.workerExternalId = workerExternalId;
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
        return workerExternalId.equals(that.workerExternalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerExternalId);
    }
}