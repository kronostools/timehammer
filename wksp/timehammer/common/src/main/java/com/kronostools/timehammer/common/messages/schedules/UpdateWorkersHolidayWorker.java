package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.Company;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@JsonDeserialize(builder = UpdateWorkersHolidayWorkerBuilder.class)
public class UpdateWorkersHolidayWorker extends ProcessableBatchScheduleMessage {
    private LocalDate holidayCandidate;
    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private CheckHolidayPhase checkHolidayPhase;
    private SaveHolidayPhase saveHolidayPhase;

    UpdateWorkersHolidayWorker(final LocalDateTime timestamp, final String name, final UUID executionId, final Integer batchSize) {
        super(timestamp, name, executionId, batchSize);
    }

    public boolean processedSuccessfully() {
        return Optional.ofNullable(saveHolidayPhase)
                .map(SaveHolidayPhase::isSuccessful)
                .orElse(false);
    }

    public LocalDate getHolidayCandidate() {
        return holidayCandidate;
    }

    public void setHolidayCandidate(LocalDate holidayCandidate) {
        this.holidayCandidate = holidayCandidate;
    }

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public void setWorkerInternalId(String workerInternalId) {
        this.workerInternalId = workerInternalId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public void setWorkerExternalId(String workerExternalId) {
        this.workerExternalId = workerExternalId;
    }

    public CheckHolidayPhase getCheckHolidayPhase() {
        return checkHolidayPhase;
    }

    public void setCheckHolidayPhase(CheckHolidayPhase checkHolidayPhase) {
        this.checkHolidayPhase = checkHolidayPhase;
    }

    public SaveHolidayPhase getSaveHolidayPhase() {
        return saveHolidayPhase;
    }

    public void setSaveHolidayPhase(SaveHolidayPhase saveHolidayPhase) {
        this.saveHolidayPhase = saveHolidayPhase;
    }
}