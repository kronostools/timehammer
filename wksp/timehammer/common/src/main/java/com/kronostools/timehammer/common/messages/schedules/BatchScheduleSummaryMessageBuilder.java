package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class BatchScheduleSummaryMessageBuilder extends AbstractScheduleSummaryMessageBuilder<BatchScheduleSummaryMessageBuilder> {
    private int batchSize;
    private int processedOk;
    private int processedKo;

    public static BatchScheduleSummaryMessage copyAndBuild(final ProcessableBatchScheduleMessage processableBatchScheduleMessage) {
        return Optional.ofNullable(processableBatchScheduleMessage)
                .map(pbsm -> BatchScheduleSummaryMessageBuilder.copy(pbsm).build())
                .orElse(null);
    }

    public static BatchScheduleSummaryMessageBuilder copy(final ProcessableBatchScheduleMessage processableBatchScheduleMessage) {
        return Optional.ofNullable(processableBatchScheduleMessage)
                .map(pbsm -> new BatchScheduleSummaryMessageBuilder()
                        .generated(pbsm.getGenerated())
                        .name(pbsm.getName())
                        .executionId(pbsm.getExecutionId())
                        .batchSize(pbsm.getBatchSize()))
                .orElse(null);
    }

    public static BatchScheduleSummaryMessage copyAndBuild(final BatchScheduleSummaryMessage batchScheduleSummaryMessage) {
        return Optional.ofNullable(batchScheduleSummaryMessage)
                .map(bssm -> BatchScheduleSummaryMessageBuilder.copy(bssm).build())
                .orElse(null);
    }

    public static BatchScheduleSummaryMessageBuilder copy(final BatchScheduleSummaryMessage batchScheduleSummaryMessage) {
        return Optional.ofNullable(batchScheduleSummaryMessage)
                .map(bssm -> new BatchScheduleSummaryMessageBuilder()
                        .generated(bssm.getGenerated())
                        .name(bssm.getName())
                        .executionId(bssm.getExecutionId())
                        .endTimestamp(bssm.getEndTimestamp())
                        .batchSize(bssm.getBatchSize())
                        .processedOk(bssm.getProcessedOk())
                        .processedKo(bssm.getProcessedKo()))
                .orElse(null);
    }

    public BatchScheduleSummaryMessageBuilder batchSize(final int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public BatchScheduleSummaryMessageBuilder processedOk(final int processedOk) {
        this.processedOk = processedOk;
        return this;
    }

    public BatchScheduleSummaryMessageBuilder processedKo(final int processedKo) {
        this.processedKo = processedKo;
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
        final BatchScheduleSummaryMessage result = new BatchScheduleSummaryMessage(generated, name, executionId, endTimestamp, batchSize);
        result.setProcessedOk(processedOk);
        result.setProcessedKo(processedKo);

        return result;
    }
}