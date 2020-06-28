package com.kronostools.timehammer.common.messages.updatePassword;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

@JsonDeserialize(builder = CheckUpdatePasswordRequestPhaseBuilder.class)
public class CheckUpdatePasswordRequestPhase extends Phase<SimpleResult> {
    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private String chatId;

    CheckUpdatePasswordRequestPhase(final SimpleResult result, final String errorMessage) {
        super(result, errorMessage);
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

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}