package com.kronostools.timehammer.common.messages.telegramchatbot.model;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.utils.CommonUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@JsonPOJOBuilder(withPrefix = "")
public class KeyboardOptionBuilder {
    private static final String SEPARATOR = "#";

    private String answerCode;
    private Company company;
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
                .map(ko -> {
                    final String[] codeParts = ko.getCode().split(SEPARATOR);

                    return new KeyboardOptionBuilder()
                            .answerCode(codeParts[0])
                            .text(ko.getText())
                            .company(Company.fromCode(codeParts[1]));
                })
                .orElse(null);
    }

    public KeyboardOptionBuilder answerCode(final String code) {
        this.answerCode = code;
        return this;
    }

    public KeyboardOptionBuilder text(final String text) {
        this.text = text;
        return this;
    }

    public KeyboardOptionBuilder company(final Company company) {
        this.company = company;
        return this;
    }

    public KeyboardOption build() {
        return new KeyboardOption(CommonUtils.stringFormat("{}{}{}", answerCode, SEPARATOR, company.getCode()), text);
    }
}
