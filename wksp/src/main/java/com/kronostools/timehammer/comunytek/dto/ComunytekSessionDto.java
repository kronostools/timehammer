package com.kronostools.timehammer.comunytek.dto;

public class ComunytekSessionDto {
    private final String username;
    private final String fullname;
    private final String sessionId;

    ComunytekSessionDto(final String username, final String fullname, final String sessionId) {
        this.username = username;
        this.fullname = fullname;
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getSessionId() {
        return sessionId;
    }

    public static ComunytekSessionDto fromResponse(final String username, final String response) {
        final String[] responseParts = response.split("\n");

        return new ComunytekSessionDto(username, responseParts[0], responseParts[2]);
    }

    @Override
    public String toString() {
        return "ComunytekSessionDto{" +
                "username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}