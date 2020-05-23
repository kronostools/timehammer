package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotInputMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotInputMessageBuilder;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.core.dao.TrashMessageDao;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommandPreprocessProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(CommandPreprocessProcessor.class);

    private final TrashMessageDao trashMessageDao;
    private final TimeMachineService timeMachineService;

    public CommandPreprocessProcessor(final TrashMessageDao trashMessageDao,
                                      final TimeMachineService timeMachineService) {
        this.trashMessageDao = trashMessageDao;
        this.timeMachineService = timeMachineService;
    }

    @Incoming(Channels.COMMAND_PREPROCESS)
    @Outgoing(Channels.COMMAND_ID_OUT)
    public Uni<Message<TelegramChatbotInputMessage>> process(final Message<TelegramChatbotInputMessage> message) {
        final TelegramChatbotInputMessage inputMessage = TelegramChatbotInputMessageBuilder.copy(message.getPayload()).build();

        if (inputMessage.isCommandMissing() || inputMessage.isCommandUnknown()) {
            LOG.info("Saving input message in trash messages table");

            return trashMessageDao
                    .insertMessage(inputMessage.getChatId(), inputMessage.getGenerated(), inputMessage.getText())
                    .map(upsertResult -> Message.of(inputMessage, message::ack));
        } else {
            return Uni.createFrom().item(Message.of(inputMessage, message::ack));
        }
    }
}