package com.kronostools.timehammer.telegramchatbot.lifecycle;

import com.kronostools.timehammer.common.constants.ChatbotCommand;
import com.kronostools.timehammer.telegramchatbot.client.TelegramClient;
import com.kronostools.timehammer.telegramchatbot.model.MyCommand;
import com.kronostools.timehammer.telegramchatbot.model.SetMyCommandsRequest;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class TelegramChatbotLifecycle {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramChatbotLifecycle.class);

    final TelegramClient telegramClient;

    public TelegramChatbotLifecycle(final TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    void onStartup(@Observes StartupEvent event) {
        LOG.info("BEGIN onStartup");

        LOG.debug("Republishing chatbot commands ...");
        final List<MyCommand> commands = Stream.of(ChatbotCommand.values())
                .filter(ChatbotCommand::isVisible)
                .map(c -> new MyCommand(c.getCommandText(), c.getDescription()))
                .collect(Collectors.toList());

        telegramClient.setMyCommands(new SetMyCommandsRequest(commands))
                .await().indefinitely();

        LOG.info("END onStartup");
    }
}