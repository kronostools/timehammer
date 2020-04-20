package com.kronostools.timehammer.chatbot.processor;

import com.kronostools.timehammer.chatbot.enums.ChatbotCommand;
import com.kronostools.timehammer.chatbot.service.NotificationService;
import com.kronostools.timehammer.chatbot.utils.RoutesUtils;
import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.exceptions.ChatbotAlreadyRegisteredException;
import com.kronostools.timehammer.service.AuthService;
import com.kronostools.timehammer.utils.Utils;
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
    public void process(Exchange exchange) throws Exception {
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
                            // TODO: store message in a constant or create a utility method to generate predefined messages
                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, "El registro se ha cancelado satisfactoriamente. Escriba /register para registrarse de nuevo (o con otra cuenta).");
                        } else if (chatbotCommand == ChatbotCommand.SETTINGS) {
                            // TODO
                        }
                    } else {
                        LOG.info("Authentication is required but no user is logged in");

                        // TODO: store message in a constant or create a utility method to generate predefined messages
                        outgoingMessage = NotificationService.getOutgoingMessage(chatId, "El commando que intentas ejecutar requiere estar registrado. /register para registrarse.");
                    }
                } else {
                    LOG.info("Authentication is not required");

                    if (chatbotCommand == ChatbotCommand.START) {
                        if (RoutesUtils.isLoggedIn(exchange)) {
                            // TODO: store message in a constant or create a utility method to generate predefined messages
                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, "Hola de nuevo. ¿Te acuerdas de mi? Para obtener más información sobre cómo funciona este chatbot, envíe /help.");
                        } else {
                            // TODO: store message in a constant or create a utility method to generate predefined messages
                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, "¡Bienvenido! Este chatbot te facilitará la vida para cumplimentar los fichajes. Será necesario un registro para poder usarlo, por favor, envíe /register para registrarse. Para obtener más información sobre cómo funciona este chatbot, envíe /help.");
                        }
                    } else if (chatbotCommand == ChatbotCommand.REGISTER) {
                        try {
                            ChatbotRegistrationResponseVo chatbotRegistrationResponse = authService.newChatbotRegistration(chatId);

                            // TODO: store message in a constant or create a utility method to generate predefined messages
                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, Utils.stringFormat("Para continuar con el registro accede [aquí]({})", chatbotRegistrationResponse.getLoginUrl()));
                        } catch (ChatbotAlreadyRegisteredException e) {
                            WorkerVo worker = RoutesUtils.getWorker(exchange);

                            // TODO: store message in a constant or create a utility method to generate predefined messages
                            outgoingMessage = NotificationService.getOutgoingMessage(chatId, Utils.stringFormat("Ya se ha hecho un registro para {}. /unregister para cancelar el registro.", worker.getExternalId()));
                        }
                    } else if (chatbotCommand == ChatbotCommand.HELP) {
                        // TODO: store message in a constant or create a utility method to generate predefined messages
                        outgoingMessage = NotificationService.getOutgoingMessage(chatId, Utils.stringFormat("Para conocer cómo funciona este chatbot accede al [FAQ]({}).", timehammerConfig.getHelpUrl()));
                    }
                }
            } else {
                LOG.debug("Unrecognized command");

                // TODO: store message in a constant or create a utility method to generate predefined messages
                outgoingMessage = NotificationService.getOutgoingMessage(chatId, "No sé qué hacer con ese mensaje recibido. /help si necesitas ayuda sobre cómo usar este chatbot.");

                // TODO: insert message in trash_message table
            }
        } else {
            LOG.debug("Message has not any command");

            if (RoutesUtils.isLoggedIn(exchange)) {
                LOG.debug("Message from logged in user");

                // TODO: Verificar si tiene preguntas pendientes de responder
                // TODO: store message in a constant or create a utility method to generate predefined messages
                outgoingMessage = NotificationService.getOutgoingMessage(chatId, "No sé qué hacer con ese mensaje recibido. /help si necesitas ayuda sobre cómo usar este chatbot.");
            } else {
                LOG.debug("Message from an unknown user");

                // TODO: store message in a constant or create a utility method to generate predefined messages
                outgoingMessage = NotificationService.getOutgoingMessage(chatId, "No sé quién eres. Es necesario /register para usar este chatbot. /help para obtener más información.");
            }

            // TODO: insert message in trash_message table
        }

        exchange.getMessage().setBody(outgoingMessage);
    }
}