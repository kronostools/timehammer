package com.kronostools.timehammer.commandprocessor.processors;

import com.kronostools.timehammer.commandprocessor.config.TimehammerDomainConfig;
import com.kronostools.timehammer.commandprocessor.model.ChatbotRegistrationRequest;
import com.kronostools.timehammer.commandprocessor.service.RegistrationRequestService;
import com.kronostools.timehammer.commandprocessor.service.UpdatePasswordRequestService;
import com.kronostools.timehammer.common.constants.ChatbotCommand;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.messages.constants.ChatbotMessages;
import com.kronostools.timehammer.common.messages.telegramchatbot.*;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.common.utils.CommonUtils;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalTime;

@ApplicationScoped
public class CommandProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(CommandProcessor.class);

    private final RegistrationRequestService registrationRequestService;
    private final UpdatePasswordRequestService updatePasswordRequestService;
    private final TimehammerDomainConfig timehammerDomainConfig;

    public CommandProcessor(final RegistrationRequestService registrationRequestService,
                            final UpdatePasswordRequestService updatePasswordRequestService,
                            final TimehammerDomainConfig timehammerDomainConfig) {
        this.registrationRequestService = registrationRequestService;
        this.updatePasswordRequestService = updatePasswordRequestService;
        this.timehammerDomainConfig = timehammerDomainConfig;
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
                        } else if (chatbotCommand == ChatbotCommand.UPDATE_SETTINGS) {
                            final String url = timehammerDomainConfig.getSettingsUrl(worker.getWorkerInternalId());

                            notificationMessage = TelegramChatbotNotificationMessageBuilder
                                    .copy(inputMessage)
                                    .text(ChatbotMessages.COMMAND_UPDATE_SETTINGS_INIT(url))
                                    .build();
                        } else if (chatbotCommand == ChatbotCommand.TODAY_SETTINGS) {
                            final String day = CommonUtils.stringFormat("Dia: {} ({})", CommonDateTimeUtils.formatDateToChatbot(worker.getDate()), CommonDateTimeUtils.formatDayOfWeekToChatbot(worker.getDate().getDayOfWeek()));

                            final String workTime;
                            if (worker.workToday()) {
                                final LocalTime workStart = CommonDateTimeUtils.getDateTimeAtZone(worker.getDate(), worker.getWorkStart(), SupportedTimezone.EUROPE_MADRID).toLocalTime();
                                final LocalTime workEnd = CommonDateTimeUtils.getDateTimeAtZone(worker.getDate(), worker.getWorkEnd(), SupportedTimezone.EUROPE_MADRID).toLocalTime();

                                workTime = CommonUtils.stringFormat("Trabajo: {} - {}", CommonDateTimeUtils.formatTimeToChatbot(workStart), CommonDateTimeUtils.formatTimeToChatbot(workEnd));
                            } else {
                                workTime = "Trabajo hoy: No";
                            }

                            final String lunchTime;
                            if (worker.lunchToday()) {
                                final LocalTime lunchStart = CommonDateTimeUtils.getDateTimeAtZone(worker.getDate(), worker.getLunchStart(), SupportedTimezone.EUROPE_MADRID).toLocalTime();
                                final LocalTime lunchEnd = CommonDateTimeUtils.getDateTimeAtZone(worker.getDate(), worker.getLunchEnd(), SupportedTimezone.EUROPE_MADRID).toLocalTime();

                                lunchTime = CommonUtils.stringFormat("Comida: {} - {}", CommonDateTimeUtils.formatTimeToChatbot(lunchStart), CommonDateTimeUtils.formatTimeToChatbot(lunchEnd));
                            } else {
                                lunchTime = "Comida hoy: No";
                            }

                            final String workerHoliday = CommonUtils.stringFormat("Vacación hoy: {}", worker.isWorkerHoliday() ? "Sí" : "No");
                            final String cityHoliday = CommonUtils.stringFormat("Festivo hoy: {}", worker.isCityHoliday() ? "Sí" : "No");

                            notificationMessage = TelegramChatbotNotificationMessageBuilder
                                    .copy(inputMessage)
                                    .text(ChatbotMessages.COMMAND_TODAY_SETTINGS(day, workTime, lunchTime, workerHoliday, cityHoliday))
                                    .build();
                        } else if (chatbotCommand == ChatbotCommand.UPDATE_PASSWORD) {
                            final String requestId = updatePasswordRequestService.newUpdatePasswordRequest(worker.getWorkerInternalId(), chatId, worker.getCompany(), worker.getWorkerExternalId());

                            final String url = timehammerDomainConfig.getUpdatePasswordUrl(requestId);

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
                            final ChatbotRegistrationRequest chatbotRegistrationRequest = registrationRequestService.newRegistrationRequest(chatId);

                            final String registerUrl = timehammerDomainConfig.getRegisterUrl(chatbotRegistrationRequest.getWorkerInternalId());

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