package com.kronostools.timehammer.model;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "WorkerHoliday")
@Table(name = "worker_holiday")
public class WorkerHoliday {
    @EmbeddedId
    private WorkerHolidayId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_external_id", insertable = false, updatable = false)
    private Worker worker;

    public WorkerHolidayId getId() {
        return id;
    }

    public void setId(WorkerHolidayId id) {
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
        WorkerHoliday that = (WorkerHoliday) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}