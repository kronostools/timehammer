package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.ValidationError;
import com.kronostools.timehammer.common.messages.ValidationErrorBuilder;
import com.kronostools.timehammer.common.messages.registration.ValidateRegistrationRequestPhase;
import com.kronostools.timehammer.common.messages.registration.ValidateRegistrationRequestPhaseBuilder;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessageBuilder;
import com.kronostools.timehammer.common.messages.registration.forms.RegistrationRequestForm;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.core.dao.CityService;
import com.kronostools.timehammer.core.model.CityMultipleResult;
import io.smallrye.mutiny.Uni;
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

    private static final String DEFAULT_TIMETABLE_NAME = "defaultTimetable";

    private final Emitter<WorkerRegistrationRequestMessage> workerRegisterRouteChannel;
    private final Emitter<WorkerRegistrationRequestMessage> workerRegisterNotifyChannel;
    private final CityService cityService;

    public RegistrationRequestValidatorProcessor(@Channel(Channels.WORKER_REGISTER_VALIDATE_NOTIFY) final Emitter<WorkerRegistrationRequestMessage> workerRegisterNotifyChannel,
                                                 @Channel(Channels.WORKER_REGISTER_ROUTE) final Emitter<WorkerRegistrationRequestMessage> workerRegisterRouteChannel,
                                                 final CityService cityService) {
        this.workerRegisterRouteChannel = workerRegisterRouteChannel;
        this.workerRegisterNotifyChannel = workerRegisterNotifyChannel;
        this.cityService = cityService;
    }

    @Incoming(Channels.WORKER_REGISTER_VALIDATE)
    public Uni<CompletionStage<Void>> process(final Message<WorkerRegistrationRequestMessage> message) {
        final WorkerRegistrationRequestMessage registrationRequest = WorkerRegistrationRequestMessageBuilder.copy(message.getPayload()).build();

        if (registrationRequest.getCheckRegistrationRequestPhase().isNotSuccessful()) {
            LOG.info("Nothing to validate because registration request '{}' is expired -> routing it to the end step in registration flow: '{}' ...", registrationRequest.getRegistrationRequestId(), Channels.WORKER_REGISTER_VALIDATE_NOTIFY);

            return Uni.createFrom().item(workerRegisterNotifyChannel.send(registrationRequest)
                    .handle(getMessageHandler(message, registrationRequest.getRegistrationRequestId())));
        } else {
            LOG.info("Validating registration request '{}' ...", registrationRequest.getRegistrationRequestId());

            return cityService.findAll()
                    .map(cityMultipleResult -> {
                        final List<ValidationError> validationErrors = validateForm(registrationRequest.getRegistrationRequestForm(), cityMultipleResult);

                        final ValidateRegistrationRequestPhase validationResult = new ValidateRegistrationRequestPhaseBuilder()
                                .validationErrors(validationErrors)
                                .build();

                        final WorkerRegistrationRequestMessage registrationRequestToSend = WorkerRegistrationRequestMessageBuilder.copy(registrationRequest)
                                .validateRegistrationRequestPhase(validationResult)
                                .build();

                        if (validationResult.isSuccessful()) {
                            return workerRegisterRouteChannel.send(registrationRequestToSend)
                                    .handle(getMessageHandler(message, registrationRequestToSend.getRegistrationRequestId()));
                        } else {
                            LOG.info("Registration request '{}' is invalid -> routing it to the end step in registration flow: '{}' ...", registrationRequest.getRegistrationRequestId(), Channels.WORKER_REGISTER_VALIDATE_NOTIFY);

                            return workerRegisterNotifyChannel.send(registrationRequestToSend)
                                    .handle(getMessageHandler(message, registrationRequestToSend.getRegistrationRequestId()));
                        }
                    });
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

    private List<ValidationError> validateForm(final RegistrationRequestForm registrationRequestForm, final CityMultipleResult cityMultipleResult) {
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
            if (cityMultipleResult.isNotSuccessful()) {
                validationErrors.add(new ValidationErrorBuilder()
                        .fieldName("workCity")
                        .errorMessage("Ha ocurrido un error inesperado durante la validación de los datos. Por favor, reinténtelo de nuevo, y si el error persiste contacte con el administrador del sitio.")
                        .build());
            } else {
                boolean cityExists = cityMultipleResult.getResult().stream()
                        .anyMatch(c -> c.getCode().equals(registrationRequestForm.getWorkCity()));

                if (!cityExists) {
                    validationErrors.add(new ValidationErrorBuilder()
                            .fieldName("workCity")
                            .errorMessage("La ciudad seleccionada no es válida.")
                            .build());
                }
            }
        }

        if (StringUtils.isBlank(registrationRequestForm.getCompanyCode())) {
            validationErrors.add(new ValidationErrorBuilder()
                    .fieldName("companyCode")
                    .errorMessage("La compañía es obligatoria")
                    .build());
        } else {
            if (Company.fromCode(registrationRequestForm.getCompanyCode()) == Company.UNKNOWN) {
                validationErrors.add(new ValidationErrorBuilder()
                        .fieldName("companyCode")
                        .errorMessage("La compañía seleccionada no es válida")
                        .build());
            }
        }

        validationErrors
                .addAll(validateTimetableDay(registrationRequestForm.getDefaultTimetable().getWorkStartMon(),
                        registrationRequestForm.getDefaultTimetable().getWorkEndMon(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartMon(),
                        registrationRequestForm.getDefaultTimetable().getLunchEndMon(),
                        DEFAULT_TIMETABLE_NAME, "lunes", "Mon"));

        validationErrors
                .addAll(validateTimetableDay(registrationRequestForm.getDefaultTimetable().getWorkStartTue(),
                        registrationRequestForm.getDefaultTimetable().getWorkEndTue(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartTue(),
                        registrationRequestForm.getDefaultTimetable().getLunchEndTue(),
                        DEFAULT_TIMETABLE_NAME, "martes", "Tue"));

        validationErrors
                .addAll(validateTimetableDay(registrationRequestForm.getDefaultTimetable().getWorkStartWed(),
                        registrationRequestForm.getDefaultTimetable().getWorkEndWed(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartWed(),
                        registrationRequestForm.getDefaultTimetable().getLunchEndWed(),
                        DEFAULT_TIMETABLE_NAME, "miércoles", "Wed"));

        validationErrors
                .addAll(validateTimetableDay(registrationRequestForm.getDefaultTimetable().getWorkStartThu(),
                        registrationRequestForm.getDefaultTimetable().getWorkEndThu(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartThu(),
                        registrationRequestForm.getDefaultTimetable().getLunchEndThu(),
                        DEFAULT_TIMETABLE_NAME, "jueves", "Thu"));

        validationErrors
                .addAll(validateTimetableDay(registrationRequestForm.getDefaultTimetable().getWorkStartFri(),
                        registrationRequestForm.getDefaultTimetable().getWorkEndFri(),
                        registrationRequestForm.getDefaultTimetable().getLunchStartFri(),
                        registrationRequestForm.getDefaultTimetable().getLunchEndFri(),
                        DEFAULT_TIMETABLE_NAME, "viernes", "Fri"));

        return validationErrors;
    }

    private List<ValidationError> validateTimetableDay(final String workStart, final String workEnd, final String lunchStart, final String lunchEnd,
                                                       final String timetableName, final String day, final String dayShort) {
        final List<ValidationError> validationErrors = new ArrayList<>();

        final boolean validWorkStartMon = CommonDateTimeUtils.isTimeFromFormValid(workStart);
        final boolean validWorkEndMon = CommonDateTimeUtils.isTimeFromFormValid(workEnd);

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

        final boolean validWorkInterval = CommonDateTimeUtils.isTimeIntervalFromFormValid(workStart, workEnd);

        if (validWorkStartMon
                && validWorkEndMon
                && !validWorkInterval) {
            validationErrors.add(new ValidationErrorBuilder()
                    .fieldName(timetableName + ".work" + dayShort)
                    .errorMessage("La hora de inicio de trabajo tiene que ser anterior a la de fin")
                    .build());
        }

        if (lunchStart != null && lunchEnd != null) {
            final boolean validLunchStartMon = CommonDateTimeUtils.isTimeFromFormValid(lunchStart);
            final boolean validLunchEndMon = CommonDateTimeUtils.isTimeFromFormValid(lunchEnd);

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

            final boolean validLunchInterval = CommonDateTimeUtils.isTimeIntervalFromFormValid(lunchStart, lunchEnd);

            if (validLunchStartMon
                    && validLunchEndMon
                    && !validLunchInterval) {
                validationErrors.add(new ValidationErrorBuilder()
                        .fieldName(timetableName + ".lunch" + dayShort)
                        .errorMessage("La hora de inicio de la comida tiene que ser anterior a la de fin")
                        .build());
            }

            if (validWorkInterval
                    && validLunchInterval
                    && !CommonDateTimeUtils.isTimeIntervalFromFormWithin(lunchStart, lunchEnd, workStart, workEnd)) {
                validationErrors.add(new ValidationErrorBuilder()
                        .fieldName(timetableName + ".lunch" + dayShort)
                        .errorMessage("La hora de la comida tiene que estar comprendida dentro del horario de trabajo")
                        .build());
            }
        }

        return validationErrors;
    }
}