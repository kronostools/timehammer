package com.kronostools.timehammer.statemachine.model.deserializers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.kronostools.timehammer.common.messages.constants.StatusContext;
import com.kronostools.timehammer.common.messages.constants.StatusContextAction;
import com.kronostools.timehammer.statemachine.model.WaitId;

import java.io.IOException;

public class WaitIdDeserializer extends KeyDeserializer {
    @Override
    public WaitId deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        final String[] keyParts = key.split("#");
        final String workerIternalId = keyParts[0];
        final StatusContext statusContext = StatusContext.valueOf(keyParts[1]);
        final StatusContextAction statusContextAction = StatusContextAction.valueOf(keyParts[2]);

        return new WaitId(workerIternalId, statusContext, statusContextAction);
    }
}