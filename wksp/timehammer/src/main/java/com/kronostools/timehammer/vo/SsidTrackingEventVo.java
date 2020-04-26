package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.enums.SsidTrackingEventType;
import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDateTime;

public class SsidTrackingEventVo {
    private final String workerInternalId;
    private final SsidTrackingEventType eventType;
    private final LocalDateTime occurred;

    public SsidTrackingEventVo(final String workerInternalId, final SsidTrackingEventType eventType, final LocalDateTime occurred) {
        this.workerInternalId = workerInternalId;
        this.eventType = eventType;
        this.occurred = occurred;
    }

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public SsidTrackingEventType getEventType() {
        return eventType;
    }

    public LocalDateTime getOccurred() {
        return occurred;
    }

    @Override
    public String toString() {
        return "SsidTrackingEventVo{" +
                "workerInternalId='" + workerInternalId + '\'' +
                ", eventType=" + eventType +
                ", occurred=" + TimeMachineService.formatDateTimeFull(occurred) +
                '}';
    }
}