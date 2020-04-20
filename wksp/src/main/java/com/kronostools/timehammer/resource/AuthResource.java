package com.kronostools.timehammer.resource;

import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.dto.CityDto;
import com.kronostools.timehammer.dto.FormResponse;
import com.kronostools.timehammer.dto.RegistrationForm;
import com.kronostools.timehammer.exceptions.ChatbotAlreadyRegisteredException;
import com.kronostools.timehammer.service.AuthService;
import com.kronostools.timehammer.service.CityService;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.utils.Constants;
import com.kronostools.timehammer.utils.Utils;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/auth")
public class AuthResource {
    private static final Logger LOG = LoggerFactory.getLogger(AuthResource.class);

    private final TimehammerConfig timehammerConfig;
    private final AuthService authService;
    private final CityService cityService;

    public AuthResource(final TimehammerConfig timehammerConfig,
                        final AuthService authService,
                        final CityService cityService) {
        this.timehammerConfig = timehammerConfig;
        this.authService = authService;
        this.cityService = cityService;
    }

    @ResourcePath("register.html")
    Template registerTemplate;

    @ResourcePath("registration_successful.html")
    Template registrationSuccessfulTemplate;

    @GET
    @Path("/mock/chatbotRegistration")
    @Produces(MediaType.APPLICATION_JSON)
    public String chatbotRegistration(@QueryParam("chatId") String chatId) {
        try {
            return authService.newChatbotRegistration(chatId).getLoginUrl();
        } catch (ChatbotAlreadyRegisteredException e) {
            return "Ya estás logueado, para cambiar de usuario haz logout antes de hacer login";
        }
    }

    @DELETE
    @Path("/mock/chatbotRegistration/{chatId}")
    public void chatbotCancelRegistration(@PathParam("chatId") String chatId) {
        authService.cancelChatbotRegistration(chatId);
    }

    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance registrer(@QueryParam("registrationId") String registrationId) {
        List<CityDto> cities = cityService.getAllCities();

        return registerTemplate
                .data("cities", cities)
                .data("timetableMin", TimeMachineService.formatTimeSimple(timehammerConfig.getTimetable().getMin()))
                .data("timetableMax", TimeMachineService.formatTimeSimple(timehammerConfig.getTimetable().getMax()))
                .data("registrationId", registrationId);
    }

    @GET
    @Path("/registrationSuccessful")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance loginSuccessfull() {
        return registrationSuccessfulTemplate.instance();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FormResponse processRegistrationForm(RegistrationForm registrationForm) {
        if (Utils.isDemoMode()) {
            registrationForm.setExternalPassword(Constants.DEMO_PASSWORD);
        }

        return authService.processRegistrationForm(registrationForm);
    }
}