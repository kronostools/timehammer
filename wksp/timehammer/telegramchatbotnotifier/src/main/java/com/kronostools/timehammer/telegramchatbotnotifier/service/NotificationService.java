package com.kronostools.timehammer.telegramchatbotnotifier.service;

import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessageBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.telegram.model.OutgoingMessage;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class NotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    private final CamelContext camelContext;

    public NotificationService(final CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public CompletionStage<Void> notify(final Message<TelegramChatbotNotificationMessage> message) {
        final TelegramChatbotNotificationMessage telegramChatbotNotificationMessage = TelegramChatbotNotificationMessageBuilder.copy(message.getPayload()).build();

        // TODO: transform TelegramChatbotNotificationMessage into OutgoingMessage
        //final OutgoingMessage outgoingMessage = getOutgoingMessage(telegramChatbotNotificationMessage);
        final OutgoingMessage outgoingMessage = null;

        final ProducerTemplate producerTemplate = camelContext.createProducerTemplate();

        return producerTemplate.asyncSendBody("direct:notify", outgoingMessage).minimalCompletionStage().handle((Void, e) -> {
            if (e == null) {
                message.ack();
            } else {
                LOG.warn("There was an error notifying someone");
            }

            return null;
        });
    }

    /*
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
    */
}