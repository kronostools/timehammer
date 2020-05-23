package com.kronostools.timehammer.commandprocessor.processors;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.commandprocessor.config.ChatbotRegistrationRequestCacheConfig;
import com.kronostools.timehammer.commandprocessor.config.TimehammerDomainConfig;
import com.kronostools.timehammer.commandprocessor.model.ChatbotRegistrationRequest;
import com.kronostools.timehammer.common.constants.ChatbotCommand;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.ChatbotMessages;
import com.kronostools.timehammer.common.messages.telegramchatbot.*;
import com.kronostools.timehammer.common.services.TimeMachineService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class CommandProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(CommandProcessor.class);

    private final Cache<String, ChatbotRegistrationRequest> chatbotRegistrationRequestCache;
    private final TimehammerDomainConfig timehammerDomainConfig;
    private final TimeMachineService timeMachineService;

    public CommandProcessor(final ChatbotRegistrationRequestCacheConfig chatbotRegistrationRequestCacheConfig,
                            final TimehammerDomainConfig timehammerDomainConfig,
                            final TimeMachineService timeMachineService) {
        chatbotRegistrationRequestCache = Caffeine.newBuilder()
                .expireAfterWrite(chatbotRegistrationRequestCacheConfig.getExpiration().getQty(), chatbotRegistrationRequestCacheConfig.getExpiration().getUnit())
                .build();

        this.timehammerDomainConfig = timehammerDomainConfig;
        this.timeMachineService = timeMachineService;
    }

    @Incoming(Channels.COMMAND_PROCESS)
    @Outgoing(Channels.NOTIFICATION_TELEGRAM)
    public Uni<Message<TelegramChatbotNotificationMessage>> process(final Message<TelegramChatbotInputMessage> message) {
        final TelegramChatbotInputMessage inputMessage = TelegramChatbotInputMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Trying to indentify worker of message received from chat '{}' ...", inputMessage.getChatId());

        final String chatId = inputMessage.getChatId();

        TelegramChatbotNotificationMessage notificationMessage = null;

        // TODO: try to implement a Rule pattern to avoid the excessive use of if-else (see https://www.baeldung.com/java-replace-if-statements)
        if (inputMessage.isCommandPresent()) {
            LOG.debug("Message has command");

            if (inputMessage.isCommandKnown()) {
                ChatbotCommand chatbotCommand = inputMessage.getCommand();

                LOG.debug("Recognized chatbotCommand: {}", chatbotCommand.getCommandText());

                if (chatbotCommand.isAuthenticationRequired()) {
                    if (inputMessage.identifiedWorker()) {
                        final WorkerCurrentPreferencesPhase worker = inputMessage.getWorkerCurrentPreferencesPhase();

                        LOG.debug("Authentication is required and logged in user is: {}", worker.getWorkerInternalId());

                        if (chatbotCommand == ChatbotCommand.UNREGISTER) {
                            final String url = timehammerDomainConfig.getUnregisterUrl(worker.getWorkerInternalId());

                            notificationMessage = TelegramChatbotNotificationMessageBuilder
                                    .copy(inputMessage)
                                    .text(ChatbotMessages.COMMAND_UNREGISTER_INIT(url))
                                    .build();
                        } else if (chatbotCommand == ChatbotCommand.SETTINGS) {
                            final String url = timehammerDomainConfig.getSettingsUrl(worker.getWorkerInternalId());

                            notificationMessage = TelegramChatbotNotificationMessageBuilder
                                    .copy(inputMessage)
                                    .text(ChatbotMessages.COMMAND_UPDATE_SETTINGS_INIT(url))
                                    .build();
                        } else if (chatbotCommand == ChatbotCommand.UPDATE_PASSWORD) {
                            final String url = timehammerDomainConfig.getUpdatePasswordUrl(worker.getWorkerInternalId());

                            notificationMessage = TelegramChatbotNotificationMessageBuilder
                                    .copy(inputMessage)
                                    .text(ChatbotMessages.COMMAND_UPDATE_PASSWORD_INIT(url))
                                    .build();
                        }
                    } else {
                        LOG.info("Command '{}' requires being authenticated, but no worker is logged in", chatbotCommand.name());

                        notificationMessage = TelegramChatbotNotificationMessageBuilder
                                .copy(inputMessage)
                                .text(ChatbotMessages.COMMAND_REGISTRATION_REQUIRED)
                                .build();
                    }
                } else {
                    LOG.info("Authentication is not required");

                    if (chatbotCommand == ChatbotCommand.START) {
                        if (inputMessage.identifiedWorker()) {
                            notificationMessage = TelegramChatbotNotificationMessageBuilder
                                    .copy(inputMessage)
                                    .text(ChatbotMessages.COMMAND_START_REGISTERED)
                                    .build();
                        } else {
                            notificationMessage = TelegramChatbotNotificationMessageBuilder
                                    .copy(inputMessage)
                                    .text(ChatbotMessages.COMMAND_START_UNREGISTERED)
                                    .build();
                        }
                    } else if (chatbotCommand == ChatbotCommand.REGISTER) {
                        if (inputMessage.identifiedWorker()) {
                            notificationMessage = TelegramChatbotNotificationMessageBuilder
                                    .copy(inputMessage)
                                    .text(ChatbotMessages.COMMAND_REGISTER_REGISTERED(inputMessage.getWorkerCurrentPreferencesPhase().getWorkerExternalId()))
                                    .build();
                        } else {
                            final String newWorkerInternalId = UUID.randomUUID().toString();
                            final ChatbotRegistrationRequest chatbotRegistrationRequest = new ChatbotRegistrationRequest(newWorkerInternalId, chatId, timeMachineService.getNow());

                            chatbotRegistrationRequestCache.put(newWorkerInternalId, chatbotRegistrationRequest);

                            final String registerUrl = timehammerDomainConfig.getUpdatePasswordUrl(newWorkerInternalId);

                            notificationMessage = TelegramChatbotNotificationMessageBuilder
                                    .copy(inputMessage)
                                    .text(ChatbotMessages.COMMAND_REGISTER_INIT(registerUrl))
                                    .build();
                        }
                    } else if (chatbotCommand == ChatbotCommand.HELP) {
                        final String helpUrl = timehammerDomainConfig.getHelpUrl();

                        notificationMessage = TelegramChatbotNotificationMessageBuilder
                                .copy(inputMessage)
                                .text(ChatbotMessages.COMMAND_HELP(helpUrl))
                                .build();
                    }
                }
            } else {
                LOG.debug("Unrecognized command");

                notificationMessage = TelegramChatbotNotificationMessageBuilder
                        .copy(inputMessage)
                        .text(ChatbotMessages.COMMAND_UNRECOGNIZED)
                        .build();
            }
        } else {
            LOG.debug("Message has not any command");

            if (inputMessage.identifiedWorker()) {
                LOG.debug("Message from logged in user");

                notificationMessage = TelegramChatbotNotificationMessageBuilder
                        .copy(inputMessage)
                        .text(ChatbotMessages.COMMAND_MISSING_REGISTERED)
                        .build();
            } else {
                LOG.debug("Message from an unknown user");

                notificationMessage = TelegramChatbotNotificationMessageBuilder
                        .copy(inputMessage)
                        .text(ChatbotMessages.COMMAND_MISSING_UNREGISTERED)
                        .build();
            }
        }

        return Uni.createFrom().item(Message.of(notificationMessage, message::ack));
    }
}