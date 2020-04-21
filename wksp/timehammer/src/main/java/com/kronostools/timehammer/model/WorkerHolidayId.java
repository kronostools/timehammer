package com.kronostools.timehammer.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class WorkerHolidayId implements Serializable {
    @Column(name = "worker_external_id", nullable = false)
    private String workerExternalId;

    @Column(nullable = false)
    private LocalDate day;

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public void setWorkerExternalId(String workerExternalId) {
        this.workerExternalId = workerExternalId;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerHolidayId that = (WorkerHolidayId) o;
        return workerExternalId.equals(that.workerExternalId) &&
                day.equals(that.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerExternalId, day);
    }
}
