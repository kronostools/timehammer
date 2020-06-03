package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.ValidationError;
import com.kronostools.timehammer.common.messages.ValidationErrorBuilder;
import com.kronostools.timehammer.common.messages.registration.ValidateRegistrationRequestPhase;
import com.kronostools.timehammer.common.messages.registration.ValidateRegistrationRequestPhaseBuilder;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequest;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestBuilder;
import com.kronostools.timehammer.common.messages.registration.forms.RegistrationRequestForm;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

import static com.kronostools.timehammer.common.utils.CommonUtils.stringFormat;

@ApplicationScoped
public class RegistrationRequestValidatorProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationRequestValidatorProcessor.class);

    private final Emitter<WorkerRegistrationRequest> workerRegisterRouteChannel;
    private final Emitter<WorkerRegistrationRequest> workerRegisterNotifyChannel;

    public RegistrationRequestValidatorProcessor(@Channel(Channels.WORKER_REGISTER_ROUTE) final Emitter<WorkerRegistrationRequest> workerRegisterRouteChannel,
                                                 @Channel(Channels.WORKER_REGISTER_NOTIFY_OUT) final Emitter<WorkerRegistrationRequest> workerRegisterNotifyChannel) {
        this.workerRegisterRouteChannel = workerRegisterRouteChannel;
        this.workerRegisterNotifyChannel = workerRegisterNotifyChannel;
    }

    @Incoming(Channels.WORKER_REGISTER_VALIDATE)
    public CompletionStage<Void> process(final Message<WorkerRegistrationRequest> message) {
        final WorkerRegistrationRequest registrationRequest = WorkerRegistrationRequestBuilder.copy(message.getPayload()).build();

        LOG.info("Validating registration request '{}' ...", registrationRequest.getRegistrationRequestForm().getWorkerInternalId());

        final List<ValidationError> validationErrors = validateForm(registrationRequest.getRegistrationRequestForm());

        final ValidateRegistrationRequestPhase validationResult = new ValidateRegistrationRequestPhaseBuilder()
                .validationErrors(validationErrors)
                .build();

        registrationRequest.setValidateRegistrationRequestPhase(validationResult);

        if (validationResult.isSuccessful()) {
            LOG.info("Registration request '{}' is valid -> routing it to the next step in registration flow", registrationRequest.getRegistrationRequestForm().getWorkerExternalId());

            return workerRegisterRouteChannel.send(registrationRequest)
                    .handle(getMessageHandler(message, registrationRequest.getRegistrationRequestForm().getWorkerInternalId()));
        } else {
            LOG.info("Registration request '{}' is invalid -> routing it to the end step in registration flow", registrationRequest.getRegistrationRequestForm().getWorkerExternalId());

            return workerRegisterNotifyChannel.send(registrationRequest)
                    .handle(getMessageHandler(message, registrationRequest.getRegistrationRequestForm().getWorkerInternalId()));
        }
    }

    private BiFunction<? super Void, Throwable, Void> getMessageHandler(final Message<?> message, final String registrationRequestId) {
        return (Void, e) -> {
            if (e != null) {
                LOG.error(stringFormat("Exception while routing registration request '{}'", registrationRequestId), e);
            } else {
                message.ack();
                LOG.debug("Registration request '{}' routed successfully", registrationRequestId);
            }

            return null;
        };
    }

    private List<ValidationError> validateForm(final RegistrationRequestForm registrationRequestForm) {
        final List<ValidationError> validationErrors = new ArrayList<>();

        if (StringUtils.isBlank(registrationRequestForm.getWorkerExternalId())) {
            validationErrors.add(new ValidationErrorBuilder()
                    .fieldName("workerExternalId")
                    .errorMessage("El nombre de usuario es obligatorio")
                    .build());
        }

        if (StringUtils.isBlank(registrationRequestForm.getWorkerExternalPassword())) {
            validationErrors.add(new ValidationErrorBuilder()
                    .fieldName("workerExternalPassword")
                    .errorMessage("La contraseña es obligatoria")
                    .build());
        }

        if (StringUtils.isBlank(registrationRequestForm.getWorkCity())) {
            validationErrors.add(new ValidationErrorBuilder()
                    .fieldName("workCity")
                    .errorMessage("La ciudad de trabajo es obligatoria")
                    .build());
        } else {
            // TODO: validar que la ciudad es válida
        }

        validationErrors
                .addAll(validateDay(registrationRequestForm.getDefaultTimetable().getWorkStartMon(),
                        registrationRequestForm.getDefaultTimetable().getWorkEndMon(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartMon(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartMon(),
                        "defaultTimetable", "lunes", "Mon"));

        validationErrors
                .addAll(validateDay(registrationRequestForm.getDefaultTimetable().getWorkStartTue(),
                        registrationRequestForm.getDefaultTimetable().getWorkEndTue(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartTue(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartTue(),
                        "defaultTimetable", "martes", "Tue"));

        validationErrors
                .addAll(validateDay(registrationRequestForm.getDefaultTimetable().getWorkStartWed(),
                        registrationRequestForm.getDefaultTimetable().getWorkEndWed(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartWed(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartWed(),
                        "defaultTimetable", "miércoles", "Wed"));

        validationErrors
                .addAll(validateDay(registrationRequestForm.getDefaultTimetable().getWorkStartThu(),
                        registrationRequestForm.getDefaultTimetable().getWorkEndThu(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartThu(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartThu(),
                        "defaultTimetable", "jueves", "Thu"));

        validationErrors
                .addAll(validateDay(registrationRequestForm.getDefaultTimetable().getWorkStartFri(),
                        registrationRequestForm.getDefaultTimetable().getWorkEndFri(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartFri(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartFri(),
                        "defaultTimetable", "viernes", "Fri"));

        return validationErrors;
    }

    private List<ValidationError> validateDay(final String workStart, final String workEnd, final String lunchStart, final String lunchEnd,
                                              final String timetableName, final String day, final String dayShort) {
        final List<ValidationError> validationErrors = new ArrayList<>();

        final boolean validWorkStartMon = CommonDateTimeUtils.isValidTimeFromForm(workStart);
        final boolean validWorkEndMon = CommonDateTimeUtils.isValidTimeFromForm(workEnd);

        if (!validWorkStartMon) {
            validationErrors.add(new ValidationErrorBuilder()
                    .fieldName(timetableName + ".workStart" + dayShort)
                    .errorMessage("El formato de la hora de inicio de trabajo del " + day + " es inválido")
                    .build());
        }

        if (!validWorkEndMon) {
            validationErrors.add(new ValidationErrorBuilder()
                    .fieldName(timetableName + ".workEnd" + dayShort)
                    .errorMessage("El formato de la hora de fin de trabajo del " + day + " es inválido")
                    .build());
        }

        if (validWorkStartMon
                && validWorkEndMon
                && !CommonDateTimeUtils.isValidTimeIntervalFromForm(workStart, workEnd)) {
            validationErrors.add(new ValidationErrorBuilder()
                    .fieldName(timetableName + ".work" + dayShort)
                    .errorMessage("La hora de inicio de trabajo tiene que ser anterior a la de fin")
                    .build());
        }

        final boolean validLunchStartMon = CommonDateTimeUtils.isValidTimeFromForm(lunchStart);
        final boolean validLunchEndMon = CommonDateTimeUtils.isValidTimeFromForm(lunchEnd);

        if (!validLunchStartMon) {
            validationErrors.add(new ValidationErrorBuilder()
                    .fieldName(timetableName + ".lunchStart" + dayShort)
                    .errorMessage("El formato de la hora de inicio de la comida del " + day + " es inválido")
                    .build());
        }

        if (!validLunchEndMon) {
            validationErrors.add(new ValidationErrorBuilder()
                    .fieldName(timetableName + ".lunchEnd" + dayShort)
                    .errorMessage("El formato de la hora de fin de la comida del " + day + " es inválido")
                    .build());
        }

        if (validLunchStartMon
                && validLunchEndMon
                && !CommonDateTimeUtils.isValidTimeIntervalFromForm(lunchStart, lunchEnd)) {
            validationErrors.add(new ValidationErrorBuilder()
                    .fieldName(timetableName + ".lunch" + dayShort)
                    .errorMessage("La hora de inicio de la comida tiene que ser anterior a la de fin")
                    .build());
        }

        return validationErrors;
    }
}