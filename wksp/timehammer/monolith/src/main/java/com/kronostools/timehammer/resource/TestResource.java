package com.kronostools.timehammer.resource;

import com.kronostools.timehammer.chatbot.service.NotificationService;
import com.kronostools.timehammer.dto.RegistrationForm;
import com.kronostools.timehammer.dto.form.RegistrationFormValidation;
import com.kronostools.timehammer.dto.form.RegistrationFormValidationAdapter;
import com.kronostools.timehammer.dto.form.validation.RegistrationValidationOrder;
import com.kronostools.timehammer.enums.QuestionType;
import com.kronostools.timehammer.service.TimeMachineService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.Set;

@Path("/tests")
public class TestResource {
    private final NotificationService notificationService;
    private final TimeMachineService timeMachineService;
    private final Validator validator;

    public TestResource(final NotificationService notificationService,
                        final TimeMachineService timeMachineService,
                        final Validator validator) {
        this.notificationService = notificationService;
        this.timeMachineService = timeMachineService;
        this.validator = validator;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello() {
        return "hello";
    }

    @POST
    @Path("/notify/{chatId}")
    public void notify(@PathParam("chatId") String chatId, @QueryParam("message") String message) {
        notificationService.notify(chatId, message);
    }

    @POST
    @Path("/notifyWithResult/{chatId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Long notifyWithResult(@PathParam("chatId") String chatId, @QueryParam("message") String message) {
        return notificationService.notifyWithResult(chatId, message);
    }

    @POST
    @Path("/question/{chatId}")
    public void question(@PathParam("chatId") String chatId) {
        final LocalDateTime now = timeMachineService.getNow();

        notificationService.question(chatId, QuestionType.START, now);
    }

    @POST
    @Path("/validationTest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void validationTest(RegistrationForm registrationForm) {
        Set<ConstraintViolation<RegistrationFormValidation>> violations = validator.validate(new RegistrationFormValidationAdapter(registrationForm), RegistrationValidationOrder.class);

        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
    }
}