package com.kronostools.timehammer.resource;

import com.kronostools.timehammer.dto.NewSsidTrackingReportDto;
import com.kronostools.timehammer.dto.SsidTrackingEventDto;
import com.kronostools.timehammer.service.WorkerSsidTrackingInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ssidTrackingReports")
public class WorkerSsidTrackingInfoResource {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerSsidTrackingInfoResource.class);

    private final WorkerSsidTrackingInfoService workerSsidTrackingInfoService;

    public WorkerSsidTrackingInfoResource(final WorkerSsidTrackingInfoService workerSsidTrackingInfoService) {
        this.workerSsidTrackingInfoService = workerSsidTrackingInfoService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SsidTrackingEventDto newSsidTrackingReport(final NewSsidTrackingReportDto newSsidTrackingReportDto) {
        LOG.debug("Creating a new SSID report");

        return workerSsidTrackingInfoService.newSsidTrackingReport(newSsidTrackingReportDto);
    }
}