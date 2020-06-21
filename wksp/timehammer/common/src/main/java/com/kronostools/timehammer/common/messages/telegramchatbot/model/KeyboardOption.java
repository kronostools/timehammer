package com.kronostools.timehammer.common.messages.telegramchatbot.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = KeyboardOptionBuilder.class)
public class KeyboardOption {
    private final String code;
    private final String text;

    public KeyboardOption(final String code, final String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
