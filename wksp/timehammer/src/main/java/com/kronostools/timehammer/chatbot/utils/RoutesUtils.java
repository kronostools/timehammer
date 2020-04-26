package com.kronostools.timehammer.chatbot.utils;

import com.kronostools.timehammer.chatbot.enums.ChatbotCommand;
import com.kronostools.timehammer.chatbot.utils.RoutesConstants.Headers;
import com.kronostools.timehammer.enums.AnswerType;
import com.kronostools.timehammer.enums.QuestionType;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.vo.TrashMessageVo;
import com.kronostools.timehammer.vo.WorkerCurrentPreferencesVo;
import com.kronostools.timehammer.vo.WorkerVo;
import org.apache.camel.Exchange;
import org.apache.camel.component.telegram.model.IncomingMessage;

import java.util.Optional;

public class RoutesUtils {
    public static boolean hasCommand(final String message) {
        boolean result = false;

        if (message != null) {
            result = message.startsWith(RoutesConstants.Commands.COMMAND_START);
        }

        return result;
    }

    public static Boolean hasCommand(final Exchange exchange) {
        return exchange.getMessage().getHeader(Headers.COMMAND_PRESENT, Boolean.class);
    }

    public static Optional<ChatbotCommand> getCommand(final String message) {
        Optional<ChatbotCommand> result = Optional.ofNullable(null);

        if (message != null) {
            final String trimmedMessage = message.trim();

            final int firstWhitespacePos = trimmedMessage.indexOf(" ");

            String firstWord = null;

            if (firstWhitespacePos == -1) {
                firstWord = trimmedMessage;
            } else {
                firstWord = trimmedMessage.substring(0, firstWhitespacePos);
            }

            result = ChatbotCommand.fromCommandText(firstWord);
        }

        return result;
    }

    public static ChatbotCommand getCommand(final Exchange exchange) {
        return exchange.getMessage().getHeader(Headers.COMMAND, ChatbotCommand.class);
    }

    public static Boolean isLoggedIn(final Exchange exchange) {
        return exchange.getMessage().getHeader(Headers.LOGGED_IN, Boolean.class);
    }

    public static Boolean commandIsRecognized(final Exchange exchange) {
        return exchange.getMessage().getHeader(Headers.COMMAND_RECOGNIZED, Boolean.class);
    }

    public static WorkerVo getWorker(final Exchange exchange) {
        return exchange.getMessage().getHeader(Headers.WORKER, WorkerVo.class);
    }

    public static QuestionType getQuestion(final Exchange exchange) {
        return exchange.getMessage().getHeader(Headers.QUESTION, QuestionType.class);
    }

    public static AnswerType getAnswer(final Exchange exchange) {
        return exchange.getMessage().getHeader(Headers.ANSWER, AnswerType.class);
    }

    public static TrashMessageVo getTrashMessage(final IncomingMessage incomingMessage) {
        return new TrashMessageVo(incomingMessage.getChat().getId(), TimeMachineService.getDateTime(incomingMessage.getDate()), incomingMessage.getText());
    }

    public static WorkerCurrentPreferencesVo getWorkerCurrentPreferences(final Exchange exchange) {
        return exchange.getMessage().getHeader(Headers.WORKER_CURRENT_PREFERENCES, WorkerCurrentPreferencesVo.class);
    }
}