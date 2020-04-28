package com.kronostools.timehammer.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.chatbot.service.NotificationService;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.dto.ComunytekSessionDto;
import com.kronostools.timehammer.comunytek.exceptions.ComunytekAuthenticationException;
import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.dto.FormResponse;
import com.kronostools.timehammer.dto.FormResponse.FormResponseBuilder;
import com.kronostools.timehammer.dto.RegistrationForm;
import com.kronostools.timehammer.dto.UpdatePasswordForm;
import com.kronostools.timehammer.dto.form.*;
import com.kronostools.timehammer.dto.form.validation.RegistrationValidationOrder;
import com.kronostools.timehammer.exceptions.ChatbotAlreadyRegisteredException;
import com.kronostools.timehammer.utils.ChatbotMessages;
import com.kronostools.timehammer.utils.Constants;
import com.kronostools.timehammer.utils.Constants.GenericErrors;
import com.kronostools.timehammer.utils.Constants.RegistrationFormFields;
import com.kronostools.timehammer.utils.Constants.UpdatePasswordFormFields;
import com.kronostools.timehammer.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class AuthService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    private final TimehammerConfig timehammerConfig;
    private final TimeMachineService timeMachineService;
    private final WorkerService workerService;
    private final ComunytekClient comunytekClient;
    private final Validator validator;
    private final NotificationService notificationService;
    private final WorkerCredentialsService workerCredentialsService;

    private Cache<String, String> registeredChatbotsCache;
    private Cache<String, ChatbotRegistrationRequestVo> chatbotRegistrationRequestCache;

    public AuthService(final TimehammerConfig timehammerConfig,
                       final TimeMachineService timeMachineService,
                       final WorkerService workerService,
                       final ComunytekClient comunytekClient,
                       final Validator validator,
                       final NotificationService notificationService,
                       final WorkerCredentialsService workerCredentialsService) {
        this.timehammerConfig = timehammerConfig;
        this.timeMachineService = timeMachineService;
        this.workerService = workerService;
        this.comunytekClient = comunytekClient;
        this.validator = validator;
        this.notificationService = notificationService;
        this.workerCredentialsService = workerCredentialsService;
    }

    @PostConstruct
    void init() {
        registeredChatbotsCache = Caffeine.newBuilder().build();
        chatbotRegistrationRequestCache = Caffeine.newBuilder()
                .expireAfterWrite(10L, TimeUnit.MINUTES)
                .build();
    }

    public ChatbotRegistrationResponseVo newChatbotRegistration(final String chatId) throws ChatbotAlreadyRegisteredException {
        String internalId = registeredChatbotsCache.get(chatId, key -> workerService.findWorkerByChatId(key).map(WorkerVo::getInternalId).orElse(null));

        if (internalId != null ) {
            throw new ChatbotAlreadyRegisteredException();
        } else {
            internalId = UUID.randomUUID().toString();
            final ChatbotRegistrationRequestVo chatbotRegistrationRequestVo = new ChatbotRegistrationRequestVo(internalId, chatId, timeMachineService.getNow());

            chatbotRegistrationRequestCache.put(internalId, chatbotRegistrationRequestVo);

            return new ChatbotRegistrationResponseVo(timehammerConfig.getRegistrationUrl(internalId));
        }
    }

    public void cancelChatbotRegistration(final String internalId, final String chatId) {
        workerService.removeChat(internalId, chatId);
        registeredChatbotsCache.invalidate(chatId);
    }

    public ChatbotUpdatePasswordResponseVo chatbotUpdatePassword(final String internalId) {
        return new ChatbotUpdatePasswordResponseVo(timehammerConfig.getUpdatePasswordUrl(internalId));
    }

    private void validateRegistrationForm(final RegistrationForm registrationForm) {
        Set<ConstraintViolation<RegistrationFormValidation>> violations = validator.validate(new RegistrationFormValidationAdapter(registrationForm), RegistrationValidationOrder.class);

        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Optional<ChatbotRegistrationRequestVo> getChatbotRegistrationRequest(final RegistrationForm registrationForm) {
        validateRegistrationForm(registrationForm);
        return Optional.ofNullable(chatbotRegistrationRequestCache.getIfPresent(registrationForm.getInternalId()));
    }

    public FormResponse processRegistrationForm(final RegistrationForm registrationForm) {
        final FormResponseBuilder formResponseBuilder = new FormResponseBuilder();

        try {
            Optional<ChatbotRegistrationRequestVo> chatbotRegistrationRequestVo = getChatbotRegistrationRequest(registrationForm);

            if (chatbotRegistrationRequestVo.isPresent()) {
                try {
                    final ComunytekSessionDto comunytekSessionDto = comunytekClient.login(registrationForm.getExternalId(), registrationForm.getExternalPassword());

                    workerCredentialsService.updateWorkerCredentials(registrationForm.getInternalId(), registrationForm.getExternalPassword());

                    final WorkerVo workerVo = new WorkerVo(registrationForm.getInternalId(), comunytekSessionDto.getFullname(), timehammerConfig.getProfile(registrationForm.getExternalId()));

                    // One timetable is required and first is the default one
                    final TimetableForm timetableForm = registrationForm.getTimetables().get(0);

                    final WorkerPreferencesVo workerPreferencesVo = new WorkerPreferencesVo(registrationForm.getInternalId(), registrationForm.getExternalId(), registrationForm.getWorkSsid(),
                            timetableForm.getWorkStarMon(), timetableForm.getWorkEndMon(), timetableForm.getLunchStarMon(), timetableForm.getLunchEndMon(),
                            timetableForm.getWorkStarTue(), timetableForm.getWorkEndTue(), timetableForm.getLunchStarTue(), timetableForm.getLunchEndTue(),
                            timetableForm.getWorkStarWed(), timetableForm.getWorkEndWed(), timetableForm.getLunchStarWed(), timetableForm.getLunchEndWed(),
                            timetableForm.getWorkStarThu(), timetableForm.getWorkEndThu(), timetableForm.getLunchStarThu(), timetableForm.getLunchEndThu(),
                            timetableForm.getWorkStarFri(), timetableForm.getWorkEndFri(), timetableForm.getLunchStarFri(), timetableForm.getLunchEndFri(),
                            registrationForm.getWorkCity().getCode(), registrationForm.getWorkCity().getTimezone(), registrationForm.getCompany());

                    final WorkerChatVo workerChatVo = new WorkerChatVo(chatbotRegistrationRequestVo.get().getChatId());

                    workerService.registerWorker(workerVo, workerPreferencesVo, workerChatVo);

                    registeredChatbotsCache.put(chatbotRegistrationRequestVo.get().getChatId(), registrationForm.getInternalId());

                    notificationService.notify(chatbotRegistrationRequestVo.get().getChatId(), ChatbotMessages.SUCCESSFUL_REGISTRATION);
                } catch (ComunytekAuthenticationException e) {
                    LOG.warn("Incorrect credentials submitted");
                    formResponseBuilder.addFieldError(RegistrationFormFields.EXTERNAL_PASSWORD, GenericErrors.INCORRECT_CREDENTIALS);
                } catch (Exception e) {
                    LOG.error("Unexpected error processing the submitted registration form", e);
                    formResponseBuilder.addFormError(GenericErrors.UNEXPEDTED_ERROR);
                }
            } else {
                LOG.warn("Registration '{}' has expired", registrationForm.getInternalId());
                formResponseBuilder.addFormError(Constants.RegistrationErrorMessages.REGISTRATION_EXPIRED);
            }
        } catch (ConstraintViolationException e) {
            e.getConstraintViolations().forEach(cv -> {
                final String propertyPath = cv.getPropertyPath().toString();
                final String fieldId = propertyPath.substring(propertyPath.lastIndexOf(".") + 1);

                formResponseBuilder.addFormError(new FormError(fieldId, cv.getMessage()));
            });
        }

        return formResponseBuilder.build();
    }

    private void validateUpdatePasswordForm(final UpdatePasswordForm updatePasswordForm) {
        Set<ConstraintViolation<UpdatePasswordFormValidation>> violations = validator.validate(new UpdatePasswordFormValidationAdapter(updatePasswordForm));

        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
    }

    public FormResponse processUpdatePasswordForm(final UpdatePasswordForm updatePasswordForm) {
        FormResponseBuilder formResponseBuilder = new FormResponseBuilder();

        try {
            validateUpdatePasswordForm(updatePasswordForm);

            final WorkerPreferencesVo workerPreferences = workerService.getWorkerPreferencesByInternalId(updatePasswordForm.getInternalId());

            try {
                comunytekClient.login(workerPreferences.getWorkerExternalId(), updatePasswordForm.getExternalPassword());

                workerCredentialsService.updateWorkerCredentials(updatePasswordForm.getInternalId(), updatePasswordForm.getExternalPassword());
            } catch (ComunytekAuthenticationException e) {
                LOG.warn("Incorrect credentials submitted");
                formResponseBuilder.addFieldError(UpdatePasswordFormFields.EXTERNAL_PASSWORD, GenericErrors.INCORRECT_CREDENTIALS);
            } catch (Exception e) {
                LOG.error("Unexpected error processing the submitted registration form", e);
                formResponseBuilder.addFormError(GenericErrors.UNEXPEDTED_ERROR);
            }
        } catch (ConstraintViolationException e) {
            e.getConstraintViolations().forEach(cv -> {
                final String propertyPath = cv.getPropertyPath().toString();
                final String fieldId = propertyPath.substring(propertyPath.lastIndexOf(".") + 1);

                formResponseBuilder.addFormError(new FormError(fieldId, cv.getMessage()));
            });
        }

        return formResponseBuilder.build();
    }
}