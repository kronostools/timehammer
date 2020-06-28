package com.kronostools.timehammer.common.messages.updatePassword;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class CheckUpdatePasswordRequestPhaseBuilder extends PhaseBuilder<SimpleResult, CheckUpdatePasswordRequestPhaseBuilder> {
    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private String chatId;

    public static CheckUpdatePasswordRequestPhase copyAndBuild(final CheckUpdatePasswordRequestPhase checkUpdatePasswordRequestPhase) {
        return Optional.ofNullable(checkUpdatePasswordRequestPhase)
                .map(cuprp -> CheckUpdatePasswordRequestPhaseBuilder.copy(cuprp).build())
                .orElse(null);
    }

    public static CheckUpdatePasswordRequestPhaseBuilder copy(final CheckUpdatePasswordRequestPhase checkUpdatePasswordRequestPhase) {
        return Optional.ofNullable(checkUpdatePasswordRequestPhase)
                .map(cuprp -> new CheckUpdatePasswordRequestPhaseBuilder()
                        .result(cuprp.getResult())
                        .errorMessage(cuprp.getErrorMessage())
                        .workerInternalId(cuprp.getWorkerInternalId())
                        .company(cuprp.getCompany())
                        .workerExternalId(cuprp.getWorkerExternalId())
                        .chatId(cuprp.getChatId()))
                .orElse(null);
    }

    public CheckUpdatePasswordRequestPhaseBuilder workerInternalId(final String workerInternalId) {
        this.workerInternalId = workerInternalId;
        return this;
    }

    public CheckUpdatePasswordRequestPhaseBuilder company(final Company company) {
        this.company = company;
        return this;
    }

    public CheckUpdatePasswordRequestPhaseBuilder workerExternalId(final String workerExternalId) {
        this.workerExternalId = workerExternalId;
        return this;
    }

    public CheckUpdatePasswordRequestPhaseBuilder chatId(final String chatId) {
        this.chatId = chatId;
        return this;
    }

    public CheckUpdatePasswordRequestPhase build() {
        final CheckUpdatePasswordRequestPhase cuprp = new CheckUpdatePasswordRequestPhase(result, errorMessage);
        cuprp.setWorkerInternalId(workerInternalId);
        cuprp.setCompany(company);
        cuprp.setWorkerExternalId(workerExternalId);
        cuprp.setChatId(chatId);

        return cuprp;
    }
}