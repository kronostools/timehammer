package com.kronostools.timehammer.common.messages.schedules;

public class CredentialResult extends Result {
    private String externalPassword;

    public static class Builder implements ResultBuilder<CredentialResult> {
        private String externalPassword;

        Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder externalPassword(final String externalPassword) {
            this.externalPassword = externalPassword;
            return this;
        }

        public CredentialResult build() {
            final CredentialResult result = new CredentialResult();
            result.setSuccessful(true);
            result.setExternalPassword(externalPassword);

            return result;
        }

        @Override
        public CredentialResult buildUnsuccessful(final String errorMessage) {
            final CredentialResult result = new CredentialResult();
            result.setSuccessful(false);
            result.setErrorMessage(errorMessage);

            return result;
        }
    }

    public String getExternalPassword() {
        return externalPassword;
    }

    public void setExternalPassword(String externalPassword) {
        this.externalPassword = externalPassword;
    }
}