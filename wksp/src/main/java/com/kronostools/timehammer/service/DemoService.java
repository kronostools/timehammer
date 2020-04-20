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
import com.kronostools.timehammer.vo.WorkerVo;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
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

    public DemoWorkerStatusForm getWorkerStatus(final String workerExternalId, final String timezone) {
        final ComunytekStatusDto workerStatus = comunytekClient.getStatus(workerExternalId, Constants.DEMO_PASSWORD, timeMachineService.getNow());

        DemoWorkerStatusForm result = new DemoWorkerStatusForm();
        result.setExternalId(workerStatus.getUsername());
        result.setTimestamp(TimeMachineService.formatDateTime(TimeMachineService.getDateTimeAtZone(workerStatus.getTimestamp(), timezone), "dd/MM/yyyy HH:mm:ss.SSS"));
        result.setStatus(workerStatus.getStatus().getCode());

        return result;
    }

    public DemoTimestampForm getTimestampForm(final String zone) {
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
}