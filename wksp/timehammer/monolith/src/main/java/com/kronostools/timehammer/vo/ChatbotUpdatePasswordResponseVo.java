package com.kronostools.timehammer.vo;

public class ChatbotUpdatePasswordResponseVo {
    private final String updatePasswordUrl;

    public ChatbotUpdatePasswordResponseVo(final String updatePasswordUrl) {
        this.updatePasswordUrl = updatePasswordUrl;
    }

    public String getUpdatePasswordUrl() {
        return updatePasswordUrl;
    }

    @Override
    public String toString() {
        return "ChatbotRegistrationResponseVo{" +
                "updatePasswordUrl='" + updatePasswordUrl + '\'' +
                '}';
    }
}