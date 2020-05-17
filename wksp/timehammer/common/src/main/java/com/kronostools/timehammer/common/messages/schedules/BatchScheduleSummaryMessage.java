package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize(builder = BatchScheduleSummaryMessageBuilder.class)
public class BatchScheduleSummaryMessage extends AbstractScheduleSummaryMessage {
    private int batchSize;
    private int processedOk;
    private int processedKo;

    BatchScheduleSummaryMessage(final LocalDateTime generated, final String name, final UUID executionId, final LocalDateTime endTimestamp, final int batchSize) {
        super(generated, name, executionId, endTimestamp);
        this.batchSize = batchSize;
    }

    @JsonIgnore
    public boolean isFinished() {
        return (processedOk + processedKo) == batchSize;
    }

    @JsonIgnore
    public int getProcessed() {
        return processedOk + processedKo;
    }

    @JsonIgnore
    public boolean isProcessedSuccessfully() {
        return processedOk == batchSize;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
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
}