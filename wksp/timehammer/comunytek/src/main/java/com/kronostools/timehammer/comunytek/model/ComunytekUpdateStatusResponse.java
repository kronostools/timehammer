package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekStatusResult;

public class ComunytekUpdateStatusResponse extends ComunytekResponse<ComunytekStatusResult> {
    ComunytekUpdateStatusResponse(final ComunytekStatusResult simpleResult, final String errorMessage) {
        super(simpleResult, errorMessage);
    }
}