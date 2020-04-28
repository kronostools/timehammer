package com.kronostools.timehammer.chatbot.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.chatbot.utils.RoutesConstants;
import com.kronostools.timehammer.enums.AnswerType;
import com.kronostools.timehammer.enums.QuestionType;
import com.kronostools.timehammer.utils.ChatbotMessages;
import com.kronostools.timehammer.utils.Utils;
import com.kronostools.timehammer.vo.QuestionId;
import com.kronostools.timehammer.vo.QuestionVo;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.telegram.TelegramParseMode;
import org.apache.camel.component.telegram.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@ApplicationScoped
public class NotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    private final CamelContext camelContext;

    private final Cache<QuestionId, QuestionVo> questionCache;

    private final Cache<String, LocalDateTime> missingCredentialsCache;

    public NotificationService(final CamelContext camelContext) {
        this.camelContext = camelContext;

        this.questionCache = Caffeine.newBuilder()
                .expireAfterWrite(24L, TimeUnit.HOURS)
                .build();

        this.missingCredentialsCache = Caffeine.newBuilder()
                .expireAfterWrite(5L, TimeUnit.MINUTES)
                .build();
    }

    public void notify(final String chatId, final String text) {
        final OutgoingMessage message = getOutgoingMessage(chatId, text);

        notify(message);
    }

    public void notify(final OutgoingMessage message) {
        final ProducerTemplate producerTemplate = camelContext.createProducerTemplate();

        producerTemplate.sendBody("direct:notify", message);
    }

    public Long notifyWithResult(final String chatId, final String text) {
        final OutgoingMessage message = getOutgoingMessage(chatId, text);

        return notifyWithResult(message);
    }

    public Long notifyWithResult(final OutgoingMessage message) {
        final ProducerTemplate producerTemplate = camelContext.createProducerTemplate();

        final Object response = producerTemplate.sendBody("direct:notify", ExchangePattern.InOut, message);

        Long result = null;

        if (response instanceof MessageResult) {
            result = ((MessageResult) response).getMessage().getMessageId();
        }

        return result;
    }

    public void question(final Set<String> chatIds, final QuestionType questionType, final LocalDateTime timestamp) {
        chatIds.forEach(chatId -> question(chatId, questionType, timestamp));
    }

    public void question(final String chatId, final QuestionType questionType, final LocalDateTime timestamp) {
        final QuestionId questionId = new QuestionId(chatId, questionType);

        final QuestionVo existingQuestion = questionCache.getIfPresent(questionId);

        if (existingQuestion != null && existingQuestion.isExpired(timestamp) && existingQuestion.isNotAnswered()) {
            removeInlineKeyboardFromMessage(existingQuestion.getQuestionId().getChatId(), existingQuestion.getMessageId());
            questionCache.invalidate(questionId);
        }

        if (existingQuestion == null || existingQuestion.isExpired(timestamp)) {
            final OutgoingMessage message = getQuestionMessage(chatId, questionType);

            final Long messageId = notifyWithResult(message);

            final QuestionVo questionVo = new QuestionVo(questionId, timestamp, messageId);

            questionCache.put(questionId, questionVo);
        }
    }

    public void answerQuestion(final String chatId, final QuestionType questionType, final AnswerType answer, final Boolean answerProcessedSuccessfully) {
        final QuestionId questionId = new QuestionId(chatId, questionType);

        if (answer == AnswerType.Y) {
            if (answerProcessedSuccessfully) {
                questionCache.invalidate(questionId);
            }
        } else {
            final Optional<QuestionVo> question = Optional.ofNullable(questionCache.getIfPresent(questionId));

            question.ifPresent(questionVo -> questionVo.setAnswer(answer));
        }

        final OutgoingMessage message = getAnswerMessage(chatId, questionType, answer, answerProcessedSuccessfully);

        notify(message);
    }

    public static OutgoingMessage getOutgoingMessage(final String chatId, final String text) {
        return getOutgoingMessage(chatId, text, null);
    }

    private static OutgoingMessage getOutgoingMessage(final String chatId, final String text, final ReplyMarkup replyMarkup) {
        OutgoingMessage message = new OutgoingTextMessage.Builder()
                .text(Utils.isDemoMode() ? Utils.stringFormat("{}{}", RoutesConstants.Messages.DEMO_MODE_PREFIX, text) : text)
                .parseMode(TelegramParseMode.MARKDOWN.getCode())
                .replyMarkup(replyMarkup)
                .build();

        message.setChatId(chatId);

        return message;
    }

    private OutgoingMessage getAnswerMessage(final String chatId, final QuestionType question, final AnswerType answer, final Boolean answerProcessedSuccessfully) {
        String text;

        if (answerProcessedSuccessfully) {
            text = question.getAnswerOption(answer).getAnswerResponseText();
        } else {
            text = "Ha habido un problema registrando la acción. En un momento volveré a preguntar para reintentarlo.";
        }

        return getOutgoingMessage(chatId, text);
    }

    private OutgoingMessage getQuestionMessage(final String chatId, final QuestionType questionType) {
        final List<InlineKeyboardButton> buttons = questionType.getAnswerOptions().values().stream()
                .map(answerOption -> InlineKeyboardButton.builder()
                        .text(answerOption.getAnswerType().getAnswerButtonText())
                        .callbackData(Utils.stringFormat("{}{}{}", questionType.getQuestionCode(), RoutesConstants.Answers.SEPARATOR, answerOption.getAnswerType().getAnswerCode()))
                        .build())
                .collect(Collectors.toList());

        final ReplyMarkup replyMarkup = InlineKeyboardMarkup
                .builder()
                .addRow(buttons)
                .build();

        return getOutgoingMessage(chatId, questionType.getQuestionText(), replyMarkup);
    }

    public static OutgoingMessage getOutgoingMessageToRemoveInlineKeyboard(final String chatId, final Long messageId) {
        final InlineKeyboardMarkup replyMarkup = InlineKeyboardMarkup
                .builder()
                .build();

        return EditMessageReplyMarkupMessage
                .builder()
                .chatId(chatId)
                .messageId(messageId.intValue())
                .replyMarkup(replyMarkup)
                .build();
    }

    private void removeInlineKeyboardFromMessage(final String chatId, final Long messageId) {
        final OutgoingMessage outgoingMessage = NotificationService.getOutgoingMessageToRemoveInlineKeyboard(chatId, messageId);
        notify(outgoingMessage);
    }

    public void missingCredentials(final Set<String> chatIds, final LocalDateTime timestamp) {
        chatIds.forEach(chatId -> missingCredentials(chatId, timestamp));
    }

    public void missingCredentials(final String chatId, final LocalDateTime timestamp) {
        final LocalDateTime missingCredencialsLastNotified = missingCredentialsCache.getIfPresent(chatId);

        if (missingCredencialsLastNotified == null) {
            final OutgoingMessage message = getOutgoingMessage(chatId, ChatbotMessages.MISSING_PASSWORD);

            notify(message);

            missingCredentialsCache.put(chatId, timestamp);
        }
    }
}