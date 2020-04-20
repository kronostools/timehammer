package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDateTime;

public class SsidTrackingInfoVo {
    private final String workerExternalId;
    private final String ssid;
    private final LocalDateTime reported;

    public SsidTrackingInfoVo(final String workerExternalId, final String ssid, final LocalDateTime timestamp) {
        this.workerExternalId = workerExternalId;
        this.ssid = ssid;
        this.reported = timestamp;
    }

    public String getWorkerExternalId() {
        return workerExternalId;
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
                "workerExternalId='" + workerExternalId + '\'' +
                ", ssid='" + ssid + '\'' +
                ", reported=" + TimeMachineService.formatDateTimeFull(reported) +
                '}';
    }
}