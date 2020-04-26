package com.kronostools.timehammer.chatbot.processor;

import com.kronostools.timehammer.chatbot.utils.RoutesConstants.Headers;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.service.WorkerService;
import com.kronostools.timehammer.vo.WorkerCurrentPreferencesVo;
import com.kronostools.timehammer.vo.WorkerVo;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.IncomingCallbackQuery;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class TelegramChatIdInterceptor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramChatIdInterceptor.class);

    private final WorkerService workerService;

    public TelegramChatIdInterceptor(final WorkerService workerService) {
        this.workerService = workerService;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        LOG.debug("Checking if chatId is already logged in -> {}", exchange.getIn().getBody());

        IncomingMessage incomingMessage = null;

        if (IncomingCallbackQuery.class.isInstance(exchange.getIn().getBody())) {
            incomingMessage = exchange.getIn().getBody(IncomingCallbackQuery.class).getMessage();
        } else if (IncomingMessage.class.isInstance(exchange.getIn().getBody())) {
            incomingMessage = exchange.getIn().getBody(IncomingMessage.class);
        }

        Optional.ofNullable(incomingMessage).ifPresent(im -> {
            final String chatId = im.getChat().getId();
            final LocalDateTime timestamp = TimeMachineService.getDateTime(im.getDate());

            exchange.getMessage().setHeader(Headers.TIMESTAMP, timestamp);
            exchange.getMessage().setHeader(Headers.LOGGED_IN, Boolean.FALSE);

            final Optional<WorkerVo> worker = workerService.getWorkerByChatId(chatId);

            worker.ifPresent(w -> {
                final WorkerCurrentPreferencesVo workerCurrentPreferences = workerService.getWorkerCurrentPreferencesByInternalId(w.getInternalId(), timestamp);

                exchange.getMessage().setHeader(Headers.LOGGED_IN, Boolean.TRUE);
                exchange.getMessage().setHeader(Headers.WORKER, w);
                exchange.getMessage().setHeader(Headers.WORKER_CURRENT_PREFERENCES, workerCurrentPreferences);
            });
        });
    }
}