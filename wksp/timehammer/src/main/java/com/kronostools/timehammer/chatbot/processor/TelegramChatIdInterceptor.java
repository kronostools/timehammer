package com.kronostools.timehammer.chatbot.processor;

import com.kronostools.timehammer.chatbot.utils.RoutesConstants;
import com.kronostools.timehammer.service.WorkerService;
import com.kronostools.timehammer.vo.WorkerVo;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.IncomingCallbackQuery;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
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

        String chatId = null;

        if (IncomingCallbackQuery.class.isInstance(exchange.getIn().getBody())) {
            chatId = exchange.getIn().getBody(IncomingCallbackQuery.class).getMessage().getChat().getId();
        } else if (IncomingMessage.class.isInstance(exchange.getIn().getBody())) {
            chatId = exchange.getIn().getBody(IncomingMessage.class).getChat().getId();
        }

        Optional.ofNullable(chatId).ifPresent(cId -> {
            exchange.getMessage().setHeader(RoutesConstants.Headers.LOGGED_IN, Boolean.FALSE);

            final Optional<WorkerVo> worker = workerService.getWorkerByChatId(cId);

            worker.ifPresent(w -> {
                exchange.getMessage().setHeader(RoutesConstants.Headers.LOGGED_IN, Boolean.TRUE);
                exchange.getMessage().setHeader(RoutesConstants.Headers.WORKER, w);
            });
        });
    }
}