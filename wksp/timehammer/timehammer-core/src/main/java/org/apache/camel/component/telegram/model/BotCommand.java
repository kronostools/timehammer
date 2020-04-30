package org.apache.camel.component.telegram.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BotCommand implements Serializable {
    private static final long serialVersionUID = 1L;

    private String command;
    private String description;

    public BotCommand() {
    }

    public BotCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("BotCommand{");
        sb.append("command='").append(this.command).append('\'');
        sb.append(", description='").append(this.description);
        sb.append('}');
        return sb.toString();
    }

    public static BotCommand.Builder builder() {
        return new BotCommand.Builder();
    }

    public static class Builder {
        private String command;
        private String description;

        public Builder() {
        }

        public BotCommand.Builder command(String command) {
            this.command = command;
            return this;
        }

        public BotCommand.Builder description(String description) {
            this.description = description;
            return this;
        }

        public BotCommand build() {
            return new BotCommand(this.command, this.description);
        }
    }
}