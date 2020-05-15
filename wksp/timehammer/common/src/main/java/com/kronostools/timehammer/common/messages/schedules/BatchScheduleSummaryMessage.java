package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize(builder = BatchScheduleSummaryMessageBuilder.class)
public class BatchScheduleSummaryMessage extends BatchScheduleMessage {
    private int processedOk;
    private int processedKo;
    private LocalDateTime endTimestamp;

    BatchScheduleSummaryMessage(LocalDateTime timestamp, String name, UUID executionId, int batchSize) {
        super(timestamp, name, executionId, batchSize);
    }

    @JsonIgnore
    public boolean isFinished() {
        return (processedOk + processedKo) == batchSize;
    }

    @JsonIgnore
    public int getProcessed() {
        return processedOk + processedKo;
    }

    public int getProcessedOk() {
        return processedOk;
    }

    public void setProcessedOk(int processedOk) {
        this.processedOk = processedOk;
    }

    public int getProcessedKo() {
        return processedKo;
    }

    public void setProcessedKo(int processedKo) {
        this.processedKo = processedKo;
    }

    public LocalDateTime getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
}