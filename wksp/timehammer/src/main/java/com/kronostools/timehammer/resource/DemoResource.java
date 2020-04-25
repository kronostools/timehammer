package com.kronostools.timehammer.resource;

import com.kronostools.timehammer.dto.DemoTimestampForm;
import com.kronostools.timehammer.dto.DemoWorkerStatusForm;
import com.kronostools.timehammer.dto.FormResponse;
import com.kronostools.timehammer.enums.SupportedTimezone;
import com.kronostools.timehammer.service.DemoService;
import com.kronostools.timehammer.utils.Utils;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/demo")
public class DemoResource {
    private final DemoService demoService;

    public DemoResource(final DemoService demoService) {
        this.demoService = demoService;
    }

    @ResourcePath("demo.html")
    Template demoTemplate;

    @ResourcePath("error_403.html")
    Template error403Template;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance demo() {
        TemplateInstance result;

        if (Utils.isDemoMode()) {
            result = demoTemplate
                    .data("zones", demoService.getZones())
                    .data("timestampForm", demoService.getTimestampForm(getTimezone()))
                    .data("workers", demoService.getWorkers());
        } else {
            result = error403Template.instance();
        }

        return result;
    }

    @GET
    @Path("/timemachine/now")
    @Produces(MediaType.APPLICATION_JSON)
    public DemoTimestampForm getNow(@QueryParam("timezone") String timezoneName) {
        if (Utils.isDemoMode()) {
            return demoService.getTimestampForm(SupportedTimezone.fromTimezoneName(timezoneName));
        } else {
            throw new ForbiddenException();
        }
    }

    @POST
    @Path("/timemachine/now")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FormResponse updateNow(final DemoTimestampForm newTimestampFormDto) {
        if (Utils.isDemoMode()) {
            // TODO: continuar después de comer (crear un DemoTimestampVo con un SupportedTimestamp en vez del String)
            return demoService.updateNow(newTimestampFormDto);
        } else {
            throw new ForbiddenException();
        }
    }

    @GET
    @Path("/workers/{workerExternalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public DemoWorkerStatusForm getWorkerStatus(@PathParam("workerExternalId") final String workerExternalId) {
        if (Utils.isDemoMode()) {
            return demoService.getWorkerStatus(workerExternalId, getTimezone());
        } else {
            throw new ForbiddenException();
        }
    }

    @POST
    @Path("/schedules/updateWorkersHolidays")
    @Produces(MediaType.APPLICATION_JSON)
    public void updateWorkerHolidays() {
        if (Utils.isDemoMode()) {
            demoService.updateWorkersHolidays();
        } else {
            throw new ForbiddenException();
        }
    }

    @POST
    @Path("/schedules/updateWorkersStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public void updateWorkerStatus() {
        if (Utils.isDemoMode()) {
            demoService.updateWorkersStatus();
        } else {
            throw new ForbiddenException();
        }
    }

    private SupportedTimezone getTimezone() {
        return SupportedTimezone.EUROPE_MADRID;
    }
}