package com.kronostools.timehammer.config;

import com.kronostools.timehammer.chatbot.enums.ChatbotCommand;
import com.kronostools.timehammer.chatbot.restclient.TelegramRestClient;
import com.kronostools.timehammer.service.WorkerCredentialsService;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.camel.component.telegram.model.BotCommand;
import org.apache.camel.component.telegram.model.SetMyCommandsMessage;
import org.apache.camel.quarkus.core.CamelMainEvents;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class TimehammerLifecycle {
    private static final Logger LOG = LoggerFactory.getLogger(TimehammerLifecycle.class);

    private final TelegramRestClient telegramRestClient;
    final WorkerCredentialsService workerCredentialsService;

    public TimehammerLifecycle(@RestClient final TelegramRestClient telegramRestClient,
                               final WorkerCredentialsService workerCredentialsService) {
        this.telegramRestClient = telegramRestClient;
        this.workerCredentialsService = workerCredentialsService;
    }

    void onStartup(@Observes StartupEvent event) {
        LOG.info("BEGIN onStartup");

        LOG.debug("Configuring chatbot commands ...");
        final List<BotCommand> commands = Stream.of(ChatbotCommand.values())
                .filter(ChatbotCommand::isVisible)
                .map(c -> BotCommand.builder().command(c.getCommandText()).description(c.getDescription()).build())
                .collect(Collectors.toList());

        try {
            telegramRestClient.setMyCommands(SetMyCommandsMessage
                    .builder()
                    .commands(commands)
                    .build());

            LOG.info("Configured chatbot commands");
        } catch (Exception e) {
            LOG.warn("Chatbot commands could not be configured because there was an unexpected error while configuring them");
        }

        LOG.info("Dumping worker credentials to temp dump file ...");

        workerCredentialsService.load();

        LOG.info("Dumped worker credentials");

        LOG.info("END onStartup");
    }

    void onCamelAfterStart(@Observes CamelMainEvents.AfterStart event) {
        LOG.info("Left here in case it is required");
    }

    void onShutdown(@Observes ShutdownEvent event) {
        LOG.info("BEGIN onShutdown");

        LOG.debug("Loading worker credentials from temp dump file ...");

        workerCredentialsService.load();

        LOG.debug("Loaded worker credentials");

        LOG.info("END onShutdown");
    }
}