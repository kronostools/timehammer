package com.kronostools.timehammer.common.messages.telegramchatbot.model;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@JsonPOJOBuilder(withPrefix = "")
public class KeyboardOptionBuilder {
    private String code;
    private String text;

    public static List<KeyboardOption> copyAndBuild(final List<KeyboardOption> keyboardOptions) {
        return Optional.ofNullable(keyboardOptions).stream().flatMap(Collection::stream)
                .map(KeyboardOptionBuilder::copyAndBuild)
                .collect(Collectors.toList());
    }

    public static KeyboardOption copyAndBuild(final KeyboardOption keyboardOption) {
        return Optional.ofNullable(keyboardOption)
                .map(ko -> KeyboardOptionBuilder.copy(ko).build())
                .orElse(null);
    }

    public static KeyboardOptionBuilder copy(final KeyboardOption keyboardOption) {
        return Optional.ofNullable(keyboardOption)
                .map(ko -> new KeyboardOptionBuilder()
                        .code(ko.getCode())
                        .text(ko.getText()))
                .orElse(null);
    }

    public KeyboardOptionBuilder code(final String code) {
        this.code = code;
        return this;
    }

    public KeyboardOptionBuilder text(final String text) {
        this.text = text;
        return this;
    }

    public KeyboardOption build() {
        return new KeyboardOption(code, text);
    }
}
