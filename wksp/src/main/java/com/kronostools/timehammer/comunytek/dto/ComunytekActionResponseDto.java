package com.kronostools.timehammer.comunytek.dto;

import com.kronostools.timehammer.comunytek.utils.ComunytekConstants;

public class ComunytekActionResponseDto {
    private final Boolean result;

    ComunytekActionResponseDto(final Boolean result) {
        this.result = result;
    }

    public Boolean getResult() {
        return result;
    }

    public static ComunytekActionResponseDto fromResponse(final String response) {
        ComunytekActionResponseDto result;

        if (response != null && response.trim().equals(ComunytekConstants.OK)) {
            result = new ComunytekActionResponseDto(Boolean.TRUE);
        } else {
            result = new ComunytekActionResponseDto(Boolean.FALSE);
        }

        return result;
    }

    @Override
    public String toString() {
        return "ComunytekActionResponseDto{" +
                "result=" + result +
                '}';
    }
}