package com.kronostools.timehammer.vo;

public class ChatbotRegistrationResponseVo {
    private final String loginUrl;

    public ChatbotRegistrationResponseVo(final String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    @Override
    public String toString() {
        return "ChatbotRegistrationResponseVo{" +
                "loginUrl='" + loginUrl + '\'' +
                '}';
    }
}