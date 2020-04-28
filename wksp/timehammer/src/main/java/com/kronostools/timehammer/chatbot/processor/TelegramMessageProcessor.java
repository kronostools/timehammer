package com.kronostools.timehammer.chatbot.processor;

import com.kronostools.timehammer.chatbot.enums.ChatbotCommand;
import com.kronostools.timehammer.chatbot.service.NotificationService;
import com.kronostools.timehammer.chatbot.utils.RoutesUtils;
import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.exceptions.ChatbotAlreadyRegisteredException;
import com.kronostools.timehammer.service.AuthService;
import com.kronostools.timehammer.utils.ChatbotMessages;
import com.kronostools.timehammer.utils.Constants.Buses;
import com.kronostools.timehammer.vo.*;
import io.vertx.axle.core.eventbus.EventBus;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.apache.camel.component.telegram.model.OutgoingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TelegramMessageProcessor implements Processor {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramMessageProcessor.class);

    private final TimehammerConfig timehammerConfig;
    private final AuthService authService;
    private final EventBus bus;

    public TelegramMessageProcessor(final TimehammerConfig timehammerConfig,
                                    final AuthService authService,
                                    final EventBus bus) {
        this.timehammerConfig = timehammerConfig;
        this.authService = authService;
        this.bus = bus;
    }

    @Override
    public void process(Exchange exchange) {
        final IncomingMessage incomingMessage = exchange.getIn().getBody(IncomingMessage.class);

        LOG.debug("Processing message ...");

        final String chatId = incomingMessage.getChat().getId();

        OutgoingMessage outgoingMessage = null;

        // TODO: try to implement a Rule pattern to avoid the excessive use of if-else (see https://www.baeldung.com/java-replace-if-statements)
        if (RoutesUtils.hasCommand(exchange)) {
            LOG.debug("Message has command");

            if (RoutesUtils.commandIsRecognized(exchange)) {
                ChatbotCommand chatbotCommand = RoutesUtils.getCommand(exchange);

                LOG.debug("Recognized chatbotCommand: {}", chatbotCommand.getCommandText());

                if (chatbotCommand.isAuthenticationRequired()) {
                    if (RoutesUtils.isLoggedIn(exchange)) {
                        final WorkerVo worker = RoutesUtils.getWorker(exchange);

                        LOG.debug("Authentication is required and logged in user is: {}", worker.getInternalId());

                        if (chatbotCommand == ChatbotCommand.UNREGISTER) {
                            authService.cancelChatbotRegistration(worker.getInternalId(), chatId);
                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_UNREGISTER);
                        } else if (chatbotCommand == ChatbotCommand.SETTINGS) {
                            // TODO: implement this functionality
                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_UNIMPLEMENTED);
                        } else if (chatbotCommand == ChatbotCommand.UPDATE_PASSWORD) {
                            final ChatbotUpdatePasswordResponseVo chatbotUpdatePasswordResponse = authService.chatbotUpdatePassword(worker.getInternalId());
                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_UPDATE_PASSWORD(chatbotUpdatePasswordResponse.getUpdatePasswordUrl()));
                        }
                    } else {
                        LOG.info("Authentication is required but no user is logged in");

                        outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_REGISTRATION_REQUIRED);
                    }
                } else {
                    LOG.info("Authentication is not required");

                    if (chatbotCommand == ChatbotCommand.START) {
                        if (RoutesUtils.isLoggedIn(exchange)) {
                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_START_REGISTERED);
                        } else {
                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_START_UNREGISTERED);
                        }
                    } else if (chatbotCommand == ChatbotCommand.REGISTER) {
                        try {
                            final ChatbotRegistrationResponseVo chatbotRegistrationResponse = authService.newChatbotRegistration(chatId);

                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_REGISTER_INIT(chatbotRegistrationResponse.getLoginUrl()));
                        } catch (ChatbotAlreadyRegisteredException e) {
                            final WorkerCurrentPreferencesVo workerCurrentPreferences = RoutesUtils.getWorkerCurrentPreferences(exchange);

                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_REGISTER_REGISTERED(workerCurrentPreferences.getWorkerExternalId()));
                        }
                    } else if (chatbotCommand == ChatbotCommand.HELP) {
                        outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_HELP(timehammerConfig.getHelpUrl()));
                    }
                }
            } else {
                LOG.debug("Unrecognized command");

                outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_UNRECOGNIZED);

                storeTrashMessage(incomingMessage);
            }
        } else {
            LOG.debug("Message has not any command");

            if (RoutesUtils.isLoggedIn(exchange)) {
                LOG.debug("Message from logged in user");

                outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_MISSING_REGISTERED);
            } else {
                LOG.debug("Message from an unknown user");

                outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_MISSING_UNREGISTERED);
            }

            storeTrashMessage(incomingMessage);
        }

        exchange.getMessage().setBody(outgoingMessage);
    }

    private void storeTrashMessage(final IncomingMessage incomingMessage) {
        final TrashMessageVo trashMessage = RoutesUtils.getTrashMessage(incomingMessage);

        bus.publish(Buses.ADD_TRASH_MESSAGE, trashMessage);

        LOG.debug("Emited event [{}] in bus '{}'", trashMessage, Buses.ADD_TRASH_MESSAGE);
    }
}