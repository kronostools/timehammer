package com.kronostools.timehammer.service;

import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.dto.ComunytekStatusDto;
import com.kronostools.timehammer.dto.DemoTimestampForm;
import com.kronostools.timehammer.dto.DemoWorkerStatusForm;
import com.kronostools.timehammer.dto.FormResponse;
import com.kronostools.timehammer.dto.form.DemoTimestampFormValidation;
import com.kronostools.timehammer.dto.form.DemoTimestampFormValidationAdapter;
import com.kronostools.timehammer.dto.form.FormError;
import com.kronostools.timehammer.enums.SupportedTimezone;
import com.kronostools.timehammer.utils.Constants;
import com.kronostools.timehammer.utils.Utils;
import com.kronostools.timehammer.vo.WorkerCurrentPreferencesVo;
import com.kronostools.timehammer.vo.WorkerVo;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class DemoService {

    private final TimeMachineService timeMachineService;
    private final Validator validator;
    private final WorkerService workerService;
    private final ComunytekClient comunytekClient;

    public DemoService(final TimeMachineService timeMachineService,
                       final Validator validator,
                       final WorkerService workerService,
                       final ComunytekClient comunytekClient) {
        this.timeMachineService = timeMachineService;
        this.validator = validator;
        this.workerService = workerService;
        this.comunytekClient = comunytekClient;
    }

    public Set<String> getZones() {
        return Stream.of(SupportedTimezone.values())
                .map(SupportedTimezone::getTimezoneName)
                .collect(Collectors.toSet());
    }

    public Set<String> getWorkers() {
        return workerService.getAllWorkers()
                .stream()
                .map(WorkerVo::getExternalId)
                .collect(Collectors.toSet());
    }

    public DemoWorkerStatusForm getWorkerStatus(final String workerExternalId, final SupportedTimezone timezone) {
        final ComunytekStatusDto workerStatus = workerService.getWorkerStatus(workerExternalId, Constants.DEMO_PASSWORD, timeMachineService.getNow());
        final WorkerCurrentPreferencesVo currentPreferences = workerService.getWorkerCurrentPreferencesByExternalId(workerExternalId, timeMachineService.getNow());
        final Set<LocalDate> workerHolidays = workerService.getPendingWorkerHolidays(workerExternalId);

        final LocalDateTime workerStatusDateTime = TimeMachineService.getDateTimeAtZone(workerStatus.getTimestamp(), timezone);

        DemoWorkerStatusForm result = new DemoWorkerStatusForm();
        result.setExternalId(workerStatus.getUsername());
        result.setTimestamp(TimeMachineService.formatDateTime(workerStatusDateTime, "dd/MM/yyyy HH:mm:ss.SSS"));
        result.setDayOfWeek(TimeMachineService.getDayOfWeekFull(workerStatusDateTime, TimeMachineService.LOCALE_ES_ES));
        result.setStatus(workerStatus.getStatus().getText());
        result.setHolidays(workerHolidays.stream()
                .map(d -> TimeMachineService.formatDate(d, TimeMachineService.FORMAT_DDMMYYYY_SEP_FWS))
                .collect(Collectors.joining(", ")));

        if (currentPreferences.workToday()) {
            result.setWork(Utils.stringFormat("{} - {}",
                    TimeMachineService.formatTimeSimple(currentPreferences.getZonedWorkStart()),
                    TimeMachineService.formatTimeSimple(currentPreferences.getZonedWorkEnd())));
        } else {
            result.setWork(currentPreferences.getNonWorkingReason().getText());
        }

        if (currentPreferences.lunchToday()) {
            result.setLunch(Utils.stringFormat("{} - {}",
                    TimeMachineService.formatTimeSimple(currentPreferences.getZonedLunchStart()),
                    TimeMachineService.formatTimeSimple(currentPreferences.getZonedLunchEnd())));
        } else {
            result.setLunch("N/A");
        }

        return result;
    }

    public DemoTimestampForm getTimestampForm(final SupportedTimezone zone) {
        return DemoTimestampForm.fromLocalDateTime(timeMachineService.getNowAtZone(zone), zone);
    }

    public FormResponse updateNow(final DemoTimestampForm newTimestampFormDto) {
        FormResponse.FormResponseBuilder formResponseBuilder = new FormResponse.FormResponseBuilder();

        try {
            validateDemoTimestampForm(newTimestampFormDto);

            timeMachineService.timeTravelToDateTimeWithZone(newTimestampFormDto.toLocalDateTime(), newTimestampFormDto.getTimezone());
        } catch (ConstraintViolationException e) {
            e.getConstraintViolations().forEach(cv -> {
                final String propertyPath = cv.getPropertyPath().toString();
                final String fieldId = propertyPath.substring(propertyPath.lastIndexOf(".") + 1);

                formResponseBuilder.addFormError(new FormError(fieldId, cv.getMessage()));
            });
        }

        return formResponseBuilder.build();
    }

    private void validateDemoTimestampForm(final DemoTimestampForm demoTimestampForm) {
        Set<ConstraintViolation<DemoTimestampFormValidation>> violations = validator.validate(new DemoTimestampFormValidationAdapter(demoTimestampForm));

        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
    }

    public void updateWorkersStatus() {
        workerService.updateWorkersStatus(timeMachineService.getNow());
    }

    public void updateWorkersHolidays() {
        workerService.updateWorkersHolidays();
    }

    public void cleanPastWorkersHolidays() {
        workerService.cleanPastWorkersHolidaysUntil(timeMachineService.getNow());
    }
}