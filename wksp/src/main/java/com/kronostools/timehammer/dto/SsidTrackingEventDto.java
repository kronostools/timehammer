package com.kronostools.timehammer.dto;

import com.kronostools.timehammer.enums.SsidTrackingEventType;
import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDateTime;

public class SsidTrackingEventDto {
    private final String workerExternalId;
    private final SsidTrackingEventType eventType;
    private final LocalDateTime occurred;

    public SsidTrackingEventDto(final String workerExternalId, final SsidTrackingEventType eventType, final LocalDateTime occurred) {
        this.workerExternalId = workerExternalId;
        this.eventType = eventType;
        this.occurred = occurred;
    }

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public SsidTrackingEventType getEventType() {
        return eventType;
    }

    public LocalDateTime getOccurred() {
        return occurred;
    }

    @Override
    public String toString() {
        return "SsidTrackingEventDto{" +
                "workerExternalId='" + workerExternalId + '\'' +
                ", eventType=" + eventType.name() +
                ", occurred=" + TimeMachineService.formatDateTimeFull(occurred) +
                '}';
    }
}