package com.kronostools.timehammer.comunytek.model;

import java.time.LocalDateTime;
import java.util.Optional;

public class CachedWorkerCredentialsBuilder {
    private String externalPassword;
    private boolean expired;
    private LocalDateTime expiredSince;

    public static CachedWorkerCredentialsBuilder copy(final CachedWorkerCredentials cachedWorkerCredentials) {
        return Optional.ofNullable(cachedWorkerCredentials)
                .map(cwc -> new CachedWorkerCredentialsBuilder()
                        .externalPassword(cwc.getExternalPassword())
                        .expired(cwc.isExpired())
                        .expiredSince(cwc.getExpiredSince()))
                .orElse(null);
    }

    public CachedWorkerCredentialsBuilder externalPassword(final String externalPassword) {
        this.externalPassword = externalPassword;
        return this;
    }

    public CachedWorkerCredentialsBuilder expired(final boolean expired) {
        this.expired = expired;
        return this;
    }

    public CachedWorkerCredentialsBuilder expiredSince(final LocalDateTime lastUsed) {
        this.expiredSince = lastUsed;
        return this;
    }

    public CachedWorkerCredentials build() {
         final CachedWorkerCredentials result = new CachedWorkerCredentials();
         result.setExternalPassword(externalPassword);
         result.setExpired(expired);
         result.setExpiredSince(expiredSince);

         return result;
    }
}