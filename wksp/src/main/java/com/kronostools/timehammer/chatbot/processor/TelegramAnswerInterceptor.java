package com.kronostools.timehammer.chatbot.processor;

import com.kronostools.timehammer.chatbot.utils.RoutesConstants;
import com.kronostools.timehammer.enums.AnswerType;
import com.kronostools.timehammer.enums.QuestionType;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.IncomingCallbackQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class TelegramAnswerInterceptor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramAnswerInterceptor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.debug("Getting answer -> {}", exchange.getIn().getBody());

        final IncomingCallbackQuery incomingCallbackQuery = exchange.getIn().getBody(IncomingCallbackQuery.class);

        final String[] callbackDataParts = incomingCallbackQuery.getData().split(RoutesConstants.Answers.SEPARATOR);

        if (callbackDataParts.length == 2) {
            final Optional<QuestionType> questionType = QuestionType.fromQuestionCode(callbackDataParts[0]);
            final Optional<AnswerType> answerType = AnswerType.fromAnswerCode(callbackDataParts[1]);

            if (questionType.isPresent() && answerType.isPresent()) {
                exchange.getMessage().setHeader(RoutesConstants.Headers.QUESTION, questionType.get());
                exchange.getMessage().setHeader(RoutesConstants.Headers.ANSWER, answerType.get());
            }
        } else {
            LOG.error("Answer is incorrect: {}", incomingCallbackQuery.getData());
        }
    }
}