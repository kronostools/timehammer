package com.kronostools.timehammer.comunytek.model;

import java.time.LocalDateTime;
import java.util.Optional;

public class CachedWorkerCredentialsBuilder {
    private String externalPassword;
    private boolean invalid;
    private LocalDateTime invalidSince;

    public static CachedWorkerCredentialsBuilder copy(final CachedWorkerCredentials cachedWorkerCredentials) {
        return Optional.ofNullable(cachedWorkerCredentials)
                .map(cwc -> new CachedWorkerCredentialsBuilder()
                        .externalPassword(cwc.getExternalPassword())
                        .invalid(cwc.isInvalid())
                        .invalidSince(cwc.getInvalidSince()))
                .orElse(null);
    }

    public CachedWorkerCredentialsBuilder externalPassword(final String externalPassword) {
        this.externalPassword = externalPassword;
        return this;
    }

    public CachedWorkerCredentialsBuilder invalid(final boolean expired) {
        this.invalid = expired;
        return this;
    }

    public CachedWorkerCredentialsBuilder invalidSince(final LocalDateTime lastUsed) {
        this.invalidSince = lastUsed;
        return this;
    }

    public CachedWorkerCredentials build() {
         final CachedWorkerCredentials result = new CachedWorkerCredentials();
         result.setExternalPassword(externalPassword);
         result.setInvalid(invalid);
         result.setInvalidSince(invalidSince);

         return result;
    }
}