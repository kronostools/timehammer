package com.kronostools.timehammer.common.messages.schedules;

import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.PlatformMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class UpdateWorkersHolidaysWorker extends PlatformMessage {
    private UUID executionId;
    private String name;
    private Integer batchSize;

    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private String externalPassword;
    private List<LocalDate> holidays;
    private Boolean updatedSuccessfully;

    public static class Builder {
        private LocalDateTime timestamp;
        private UUID executionId;
        private String name;
        private Integer batchSize;

        private String workerInternalId;
        private Company company;
        private String workerExternalId;

        Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder timestamp(final LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder executionId(final UUID executionId) {
            this.executionId = executionId;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder batchSize(final Integer batchSize) {
            this.batchSize = batchSize;
            return this;
        }

        public Builder workerInternalId(final String workerInternalId) {
            this.workerInternalId = workerInternalId;
            return this;
        }

        public Builder workerExternalId(final String workerExternalId) {
            this.workerExternalId = workerExternalId;
            return this;
        }

        public Builder company(final Company company) {
            this.company = company;
            return this;
        }

        public UpdateWorkersHolidaysWorker build() {
            final UpdateWorkersHolidaysWorker result = new UpdateWorkersHolidaysWorker();
            result.setTimestamp(timestamp);
            result.setExecutionId(executionId);
            result.setName(name);
            result.setBatchSize(batchSize);
            result.setWorkerInternalId(workerInternalId);
            result.setCompany(company);
            result.setWorkerExternalId(workerExternalId);
            result.setUpdatedSuccessfully(Boolean.FALSE);

            return result;
        }
    }

    public UUID getExecutionId() {
        return executionId;
    }

    public void setExecutionId(UUID executionId) {
        this.executionId = executionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
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

    public String getExternalPassword() {
        return externalPassword;
    }

    public void setExternalPassword(String externalPassword) {
        this.externalPassword = externalPassword;
    }

    public List<LocalDate> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<LocalDate> holidays) {
        this.holidays = holidays;
    }

    public Boolean getUpdatedSuccessfully() {
        return updatedSuccessfully;
    }

    public void setUpdatedSuccessfully(Boolean updatedSuccessfully) {
        this.updatedSuccessfully = updatedSuccessfully;
    }
}