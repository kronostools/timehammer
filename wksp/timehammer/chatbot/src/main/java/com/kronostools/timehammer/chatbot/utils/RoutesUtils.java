package com.kronostools.timehammer.chatbot.utils;

import com.kronostools.timehammer.chatbot.constants.RoutesConstants;
import com.kronostools.timehammer.chatbot.constants.RoutesConstants.Commands;
import com.kronostools.timehammer.common.constants.ChatbotCommand;
import com.kronostools.timehammer.common.messages.chatbot.ChatbotMessage;
import org.apache.camel.Exchange;

import java.util.Optional;

public class RoutesUtils {
    public static boolean hasCommand(final String message) {
        boolean result = false;

        if (message != null) {
            result = message.startsWith(Commands.COMMAND_START);
        }

        return result;
    }

    public static Optional<ChatbotCommand> getCommand(final String message) {
        return getRawCommand(message)
                .flatMap(ChatbotCommand::fromCommandText);
    }

    public static Optional<String> getRawCommand(final String message) {
        return Optional.ofNullable(message)
                .map(m -> {
                    final String trimmedMessage = message.trim();

                    final int firstWhitespacePos = trimmedMessage.indexOf(" ");

                    final String firstWord;

                    if (firstWhitespacePos == -1) {
                        firstWord = trimmedMessage;
                    } else {
                        firstWord = trimmedMessage.substring(0, firstWhitespacePos);
                    }

                    return firstWord;
                });
    }

    public static ChatbotMessage getMessage(final Exchange exchange) {
        return exchange.getMessage()
                .getHeader(RoutesConstants.Headers.COMMAND_MESSAGE, ChatbotMessage.class);
    }
}