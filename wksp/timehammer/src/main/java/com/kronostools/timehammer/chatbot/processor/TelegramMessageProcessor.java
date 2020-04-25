package com.kronostools.timehammer.chatbot.processor;

import com.kronostools.timehammer.chatbot.enums.ChatbotCommand;
import com.kronostools.timehammer.chatbot.service.NotificationService;
import com.kronostools.timehammer.chatbot.utils.RoutesUtils;
import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.exceptions.ChatbotAlreadyRegisteredException;
import com.kronostools.timehammer.service.AuthService;
import com.kronostools.timehammer.utils.ChatbotMessages;
import com.kronostools.timehammer.vo.ChatbotRegistrationResponseVo;
import com.kronostools.timehammer.vo.WorkerVo;
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

    public TelegramMessageProcessor(final TimehammerConfig timehammerConfig,
                                    final AuthService authService) {
        this.timehammerConfig = timehammerConfig;
        this.authService = authService;
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
                        WorkerVo worker = RoutesUtils.getWorker(exchange);

                        LOG.debug("Authentication is required and logged in user is: {}", worker.getExternalId());

                        if (chatbotCommand == ChatbotCommand.UNREGISTER) {
                            authService.cancelChatbotRegistration(chatId);
                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_UNREGISTER);
                        } else if (chatbotCommand == ChatbotCommand.SETTINGS) {
                            // TODO: implement this functionality
                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_UNIMPLEMENTED);
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
                            ChatbotRegistrationResponseVo chatbotRegistrationResponse = authService.newChatbotRegistration(chatId);

                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_REGISTER_INIT(chatbotRegistrationResponse.getLoginUrl()));
                        } catch (ChatbotAlreadyRegisteredException e) {
                            WorkerVo worker = RoutesUtils.getWorker(exchange);

                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_REGISTER_REGISTERED(worker.getExternalId()));
                        }
                    } else if (chatbotCommand == ChatbotCommand.HELP) {
                        outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_HELP(timehammerConfig.getHelpUrl()));
                    }
                }
            } else {
                LOG.debug("Unrecognized command");

                outgoingMessage = NotificationService.getOutgoingMessage(chatId, ChatbotMessages.COMMAND_UNRECOGNIZED);

                // TODO: insert message in trash_message table
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

            // TODO: insert message in trash_message table
        }

        exchange.getMessage().setBody(outgoingMessage);
    }
}