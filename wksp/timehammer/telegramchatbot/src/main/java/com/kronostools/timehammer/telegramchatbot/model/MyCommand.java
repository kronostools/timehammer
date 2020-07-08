package com.kronostools.timehammer.telegramchatbot.model;

public class MyCommand {
    private final String command;
    private final String description;

    public MyCommand(final String command, final String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}