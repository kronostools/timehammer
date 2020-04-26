package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDateTime;

public class SsidTrackingInfoVo {
    private final String workerInternalId;
    private final String ssid;
    private final LocalDateTime reported;

    public SsidTrackingInfoVo(final String workerInternalId, final String ssid, final LocalDateTime timestamp) {
        this.workerInternalId = workerInternalId;
        this.ssid = ssid;
        this.reported = timestamp;
    }

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public String getSsid() {
        return ssid;
    }

    public LocalDateTime getReported() {
        return reported;
    }

    @Override
    public String toString() {
        return "WorkerSsidTrackingInfoVo{" +
                "workerInternalId='" + workerInternalId + '\'' +
                ", ssid='" + ssid + '\'' +
                ", reported=" + TimeMachineService.formatDateTimeFull(reported) +
                '}';
    }
}