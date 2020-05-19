package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class ScheduleSummaryMessageBuilder extends AbstractScheduleSummaryMessageBuilder<ScheduleSummaryMessageBuilder> {
    private boolean processedSuccessfully;

    public static ScheduleSummaryMessage copyAndBuild(final ProcessableScheduleMessage processableScheduleMessage) {
        return Optional.ofNullable(processableScheduleMessage)
                .map(psm -> ScheduleSummaryMessageBuilder.copy(psm).build())
                .orElse(null);
    }

    public static ScheduleSummaryMessageBuilder copy(final ProcessableScheduleMessage processableScheduleMessage) {
        return Optional.ofNullable(processableScheduleMessage)
                .map(psm -> new ScheduleSummaryMessageBuilder()
                        .generated(psm.getGenerated())
                        .name(psm.getName())
                        .executionId(psm.getExecutionId())
                        .processedSuccessfully(psm.processedSuccessfully()))
                .orElse(null);
    }

    public static ScheduleSummaryMessage copyAndBuild(final ScheduleSummaryMessage scheduleSummaryMessage) {
        return Optional.ofNullable(scheduleSummaryMessage)
                .map(ssm -> ScheduleSummaryMessageBuilder.copy(ssm).build())
                .orElse(null);
    }

    public static ScheduleSummaryMessageBuilder copy(final ScheduleSummaryMessage scheduleSummaryMessage) {
        return Optional.ofNullable(scheduleSummaryMessage)
                .map(ssm -> new ScheduleSummaryMessageBuilder()
                        .generated(ssm.getGenerated())
                        .name(ssm.getName())
                        .executionId(ssm.getExecutionId())
                        .endTimestamp(ssm.getEndTimestamp())
                        .processedSuccessfully(ssm.isProcessedSuccessfully()))
                .orElse(null);
    }

    public ScheduleSummaryMessageBuilder processedSuccessfully(final boolean processedSuccessfully) {
        this.processedSuccessfully = processedSuccessfully;
        return this;
    }

    public ScheduleSummaryMessage build() {
        return new ScheduleSummaryMessage(generated, name, executionId, endTimestamp, processedSuccessfully);
    }
}