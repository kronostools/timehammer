package org.apache.camel.component.telegram.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SetMyCommandsMessage extends OutgoingMessage {
    private List<BotCommand> commands;

    public SetMyCommandsMessage(List<BotCommand> commands) {
        this.commands = commands;
    }

    public List<BotCommand> getCommands() {
        return commands;
    }

    public static SetMyCommandsMessage.Builder builder() {
        return new SetMyCommandsMessage.Builder();
    }

    public String toString() {
        return "SetMyCommandsMessage{commands=" + this.commands + '}';
    }

    public static final class Builder {
        protected List<BotCommand> commands;

        public Builder() {
        }

        public SetMyCommandsMessage.Builder commands(List<BotCommand> commands) {
            this.commands = commands;
            return this;
        }

        public SetMyCommandsMessage build() {
            return new SetMyCommandsMessage(this.commands);
        }
    }
}