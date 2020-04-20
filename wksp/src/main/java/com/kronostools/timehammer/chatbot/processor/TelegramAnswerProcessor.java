package com.kronostools.timehammer.chatbot.processor;

import com.kronostools.timehammer.chatbot.converters.QuestionTypeToComunytekActionConverter;
import com.kronostools.timehammer.chatbot.service.NotificationService;
import com.kronostools.timehammer.chatbot.utils.RoutesUtils;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.dto.ComunytekActionResponseDto;
import com.kronostools.timehammer.comunytek.enums.ComunytekAction;
import com.kronostools.timehammer.enums.AnswerType;
import com.kronostools.timehammer.enums.QuestionType;
import com.kronostools.timehammer.vo.WorkerVo;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.EditMessageReplyMarkupMessage;
import org.apache.camel.component.telegram.model.IncomingCallbackQuery;
import org.apache.camel.component.telegram.model.InlineKeyboardMarkup;
import org.apache.camel.component.telegram.model.OutgoingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TelegramAnswerProcessor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramAnswerProcessor.class);

    private final NotificationService notificationService;
    private final ComunytekClient comunytekClient;
    private final QuestionTypeToComunytekActionConverter questionTypeToComunytekActionConverter;

    public TelegramAnswerProcessor(final NotificationService notificationService,
                                   final ComunytekClient comunytekClient,
                                   final QuestionTypeToComunytekActionConverter questionTypeToComunytekActionConverter) {
        this.notificationService = notificationService;
        this.comunytekClient = comunytekClient;
        this.questionTypeToComunytekActionConverter = questionTypeToComunytekActionConverter;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        final IncomingCallbackQuery incomingCallbackQuery = exchange.getIn().getBody(IncomingCallbackQuery.class);

        LOG.debug("Processing answer ...");

        final String chatId = incomingCallbackQuery.getMessage().getChat().getId();

        final WorkerVo worker = RoutesUtils.getWorker(exchange);
        final QuestionType question = RoutesUtils.getQuestion(exchange);
        final AnswerType answer = RoutesUtils.getAnswer(exchange);

        InlineKeyboardMarkup replyMarkup = InlineKeyboardMarkup
                .builder()
                .build();

        OutgoingMessage outgoingMessage = EditMessageReplyMarkupMessage
                .builder()
                .chatId(chatId)
                .messageId(incomingCallbackQuery.getMessage().getMessageId().intValue())
                .replyMarkup(replyMarkup)
                .build();

        exchange.getMessage().setBody(outgoingMessage);

        Boolean answerProcessedSuccessfully = Boolean.TRUE;

        if (answer == AnswerType.Y) {
            final ComunytekAction comunytekAction = questionTypeToComunytekActionConverter.convert(question);
            final ComunytekActionResponseDto response = comunytekClient.executeAction(worker.getExternalId(), worker.getExternalPassword(), comunytekAction);
            answerProcessedSuccessfully = response.getResult();
        }

        notificationService.answerQuestion(chatId, question, answer, answerProcessedSuccessfully);
    }
}