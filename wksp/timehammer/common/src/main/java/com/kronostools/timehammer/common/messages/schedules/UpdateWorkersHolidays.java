package com.kronostools.timehammer.common.messages.schedules;

import com.kronostools.timehammer.common.messages.PlatformMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UpdateWorkersHolidays extends PlatformMessage {
    private List<UpdateWorkersHolidaysWorker> workers;

    public UpdateWorkersHolidays() {
        this.workers = new ArrayList<>();
    }

    public UpdateWorkersHolidays(final LocalDateTime timestamp, final List<UpdateWorkersHolidaysWorker> workers) {
        super(timestamp);
        this.workers = workers;
    }

    public List<UpdateWorkersHolidaysWorker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<UpdateWorkersHolidaysWorker> workers) {
        this.workers = workers;
    }
}