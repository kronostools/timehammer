package com.kronostools.timehammer.chatbot.processor;

import com.kronostools.timehammer.chatbot.converters.QuestionTypeToComunytekActionConverter;
import com.kronostools.timehammer.chatbot.service.NotificationService;
import com.kronostools.timehammer.chatbot.utils.RoutesUtils;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.dto.ComunytekActionResponseDto;
import com.kronostools.timehammer.comunytek.enums.ComunytekAction;
import com.kronostools.timehammer.enums.AnswerType;
import com.kronostools.timehammer.enums.QuestionType;
import com.kronostools.timehammer.service.WorkerCredentialsService;
import com.kronostools.timehammer.utils.ChatbotMessages;
import com.kronostools.timehammer.vo.WorkerCurrentPreferencesVo;
import com.kronostools.timehammer.vo.WorkerVo;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.IncomingCallbackQuery;
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
    private final WorkerCredentialsService workerCredentialsService;

    public TelegramAnswerProcessor(final NotificationService notificationService,
                                   final ComunytekClient comunytekClient,
                                   final QuestionTypeToComunytekActionConverter questionTypeToComunytekActionConverter,
                                   final WorkerCredentialsService workerCredentialsService) {
        this.notificationService = notificationService;
        this.comunytekClient = comunytekClient;
        this.questionTypeToComunytekActionConverter = questionTypeToComunytekActionConverter;
        this.workerCredentialsService = workerCredentialsService;
    }

    @Override
    public void process(Exchange exchange) {
        final IncomingCallbackQuery incomingCallbackQuery = exchange.getIn().getBody(IncomingCallbackQuery.class);

        LOG.debug("Processing answer ...");

        final String chatId = incomingCallbackQuery.getMessage().getChat().getId();

        final WorkerVo worker = RoutesUtils.getWorker(exchange);
        final QuestionType question = RoutesUtils.getQuestion(exchange);
        final AnswerType answer = RoutesUtils.getAnswer(exchange);

        exchange.getMessage().setBody(NotificationService.getOutgoingMessageToRemoveInlineKeyboard(chatId, incomingCallbackQuery.getMessage().getMessageId()));

        String workerExternalPassword = workerCredentialsService.getWorkerCredentials(worker.getInternalId());

        if (workerExternalPassword != null) {
            Boolean answerProcessedSuccessfully = Boolean.TRUE;

            if (answer == AnswerType.Y) {
                final WorkerCurrentPreferencesVo workerCurrentPreferences = RoutesUtils.getWorkerCurrentPreferences(exchange);
                final ComunytekAction comunytekAction = questionTypeToComunytekActionConverter.convert(question);

                final ComunytekActionResponseDto response = comunytekClient.executeAction(workerCurrentPreferences.getWorkerExternalId(), workerExternalPassword, comunytekAction);
                answerProcessedSuccessfully = response.getResult();
            }

            notificationService.answerQuestion(chatId, question, answer, answerProcessedSuccessfully);
        } else {
            final OutgoingMessage outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.MISSING_PASSWORD);

            notificationService.notify(outgoingMessage);
        }
    }
}