package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class BatchScheduleSummaryMessageBuilder extends BatchScheduleMessageBuilder<BatchScheduleSummaryMessageBuilder> {
    private int processedOk;
    private int processedKo;
    private LocalDateTime endTimestamp;

    public static BatchScheduleSummaryMessage copyAndBuild(final BatchScheduleSummaryMessage batchScheduleSummaryMessage) {
        return Optional.ofNullable(batchScheduleSummaryMessage)
                .map(bssm -> BatchScheduleSummaryMessageBuilder.copy(bssm).build())
                .orElse(null);
    }

    public static BatchScheduleSummaryMessage copyAndBuild(final BatchScheduleMessage batchScheduleMessage) {
        return Optional.ofNullable(batchScheduleMessage)
                .map(bsm -> BatchScheduleSummaryMessageBuilder.copy(batchScheduleMessage).build())
                .orElse(null);
    }

    public static BatchScheduleSummaryMessageBuilder copy(final BatchScheduleMessage batchScheduleMessage) {
        return Optional.ofNullable(batchScheduleMessage)
                .map(bsm -> new BatchScheduleSummaryMessageBuilder()
                        .timestamp(bsm.getTimestamp())
                        .name(bsm.getName())
                        .executionId(bsm.getExecutionId())
                        .batchSize(bsm.getBatchSize()))
                .orElse(null);
    }

    public static BatchScheduleSummaryMessageBuilder copy(final BatchScheduleSummaryMessage batchScheduleSummaryMessage) {
        return Optional.ofNullable(batchScheduleSummaryMessage)
                .map(bssm -> new BatchScheduleSummaryMessageBuilder()
                        .timestamp(bssm.getTimestamp())
                        .name(bssm.getName())
                        .executionId(bssm.getExecutionId())
                        .batchSize(bssm.getBatchSize())
                        .processedOk(bssm.getProcessedOk())
                        .processedKo(bssm.getProcessedKo())
                        .endTimestamp(bssm.getEndTimestamp()))
                .orElse(null);
    }


    public BatchScheduleSummaryMessageBuilder processedOk(final int processedOk) {
        this.processedOk = processedOk;
        return this;
    }

    public BatchScheduleSummaryMessageBuilder processedKo(final int processedKo) {
        this.processedKo = processedKo;
        return this;
    }

    BatchScheduleSummaryMessageBuilder endTimestamp(final LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
        return this;
    }

    public BatchScheduleSummaryMessageBuilder process(final boolean ok, final LocalDateTime timestamp) {
        if (ok) {
            processedOk++;
        } else {
            processedKo++;
        }

        if ((processedOk + processedKo) == batchSize) {
            endTimestamp = timestamp;
        }

        return this;
    }

    public BatchScheduleSummaryMessage build() {
        final BatchScheduleSummaryMessage result = new BatchScheduleSummaryMessage(timestamp, name, executionId, batchSize);
        result.setProcessedOk(processedOk);
        result.setProcessedKo(processedKo);
        result.setEndTimestamp(endTimestamp);

        return result;
    }
}