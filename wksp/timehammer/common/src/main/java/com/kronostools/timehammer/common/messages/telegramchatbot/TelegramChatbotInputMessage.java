package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.ChatbotCommand;
import com.kronostools.timehammer.common.messages.constants.WorkerCurrentPreferencesResult;

import java.time.LocalDateTime;

@JsonDeserialize(builder = TelegramChatbotInputMessageBuilder.class)
public class TelegramChatbotInputMessage extends TelegramChatbotMessage {
    private ChatbotCommand command;
    private String rawCommand;
    private String text;
    private WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase;

    TelegramChatbotInputMessage(final LocalDateTime generated, final String chatId, final Long messageId, final String text) {
        super(generated, chatId, messageId);
        this.text = text;
    }

    public ChatbotCommand getCommand() {
        return command;
    }

    public void setCommand(ChatbotCommand command) {
        this.command = command;
    }

    public String getRawCommand() {
        return rawCommand;
    }

    public void setRawCommand(String rawCommand) {
        this.rawCommand = rawCommand;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public WorkerCurrentPreferencesPhase getWorkerCurrentPreferencesPhase() {
        return workerCurrentPreferencesPhase;
    }

    public void setWorkerCurrentPreferencesPhase(WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase) {
        this.workerCurrentPreferencesPhase = workerCurrentPreferencesPhase;
    }

    @JsonIgnore
    public boolean isCommandPresent() {
        return command != null || rawCommand != null;
    }

    @JsonIgnore
    public boolean isCommandMissing() {
        return !isCommandPresent();
    }

    @JsonIgnore
    public boolean isCommandKnown() {
        return command != null;
    }

    @JsonIgnore
    public boolean isCommandUnknown() {
        return rawCommand != null;
    }

    @JsonIgnore
    public boolean identifiedWorker() {
        return workerCurrentPreferencesPhase.getResult() == WorkerCurrentPreferencesResult.OK;
    }
}