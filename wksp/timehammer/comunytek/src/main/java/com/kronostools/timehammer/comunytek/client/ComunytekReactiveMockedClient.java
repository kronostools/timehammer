package com.kronostools.timehammer.comunytek.client;

public class ComunytekReactiveMockedClient implements ComunytekClient {

    @Override
    public boolean isMocked() {
        return true;
    }
}