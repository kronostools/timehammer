package com.kronostools.timehammer.comunytek.client.impl;

import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.service.CredentialsCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractComunytekClient implements ComunytekClient {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected final CredentialsCacheService credentialsCacheService;

    public AbstractComunytekClient(final CredentialsCacheService credentialsCacheService) {
        this.credentialsCacheService = credentialsCacheService;
    }
}