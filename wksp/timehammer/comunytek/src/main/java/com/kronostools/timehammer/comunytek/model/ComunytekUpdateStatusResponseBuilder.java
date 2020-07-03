package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekStatusResult;

public class ComunytekUpdateStatusResponseBuilder extends ComunytekResponseBuilder<ComunytekStatusResult, ComunytekUpdateStatusResponseBuilder> {

    public ComunytekUpdateStatusResponse build() {
        return new ComunytekUpdateStatusResponse(result, errorMessage);
    }
}