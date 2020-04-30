package com.kronostools.timehammer.dto;

public class UpdatePasswordForm {
    private String internalId;
    private String externalPassword;

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getExternalPassword() {
        return externalPassword;
    }

    public void setExternalPassword(String externalPassword) {
        this.externalPassword = externalPassword;
    }

    @Override
    public String toString() {
        return "UpdatePasswordForm{" +
                "internalId='" + internalId + '\'' +
                '}';
    }
}