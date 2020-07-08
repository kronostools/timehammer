package com.kronostools.timehammer.telegramchatbot.model;

import com.kronostools.timehammer.common.utils.CommonUtils;
import io.vertx.mutiny.core.buffer.Buffer;

import java.util.List;
import java.util.stream.Collectors;

public class SetMyCommandsRequest {
    private final List<MyCommand> commands;

    public SetMyCommandsRequest(final List<MyCommand> commands) {
        this.commands = commands;
    }

    public Buffer toBuffer() {
        final String commandsJson = commands.stream()
                .map(c -> CommonUtils.stringFormat("{\"command\":\"{}\",\"description\":\"{}\"}", c.getCommand(), c.getDescription()))
                .collect(Collectors.joining(","));

        return Buffer.buffer(CommonUtils.stringFormat("{\"commands\": [{}]}", commandsJson));
    }
}
