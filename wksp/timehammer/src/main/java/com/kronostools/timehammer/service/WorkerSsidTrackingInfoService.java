package com.kronostools.timehammer.service;

import com.kronostools.timehammer.dto.NewSsidTrackingReportDto;
import com.kronostools.timehammer.dto.SsidTrackingEventDto;
import com.kronostools.timehammer.manager.WorkerSsidTrackingInfoManager;
import com.kronostools.timehammer.vo.SsidTrackingInfoVo;
import com.kronostools.timehammer.vo.WorkerAndPreferencesVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerSsidTrackingInfoService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerSsidTrackingInfoService.class);

    private final TimeMachineService timeMachineService;
    private final WorkerService workerService;
    private final WorkerSsidTrackingInfoManager workerSsidTrackingInfoManager;

    public WorkerSsidTrackingInfoService(final TimeMachineService timeMachineService,
                                         final WorkerService workerService,
                                         final WorkerSsidTrackingInfoManager workerSsidTrackingInfoManager) {
        this.timeMachineService = timeMachineService;
        this.workerService = workerService;
        this.workerSsidTrackingInfoManager = workerSsidTrackingInfoManager;
    }

    public SsidTrackingEventDto newSsidTrackingReport(final NewSsidTrackingReportDto newSsidTrackingReportDto) {
        LOG.debug("BEGIN newSsidTrackingReport: [{}]", newSsidTrackingReportDto);

        // TODO: obtener el usuario de la autenticacion
        final String workerExternalId = "DCV";

        LOG.warn("Considered '{}' as reported because no security has been implemented", workerExternalId);

        WorkerAndPreferencesVo workerAndPreferencesVo = workerService.getWorkerAndPreferencesByExternalId(workerExternalId);

        SsidTrackingEventDto result = workerSsidTrackingInfoManager.updateWorkerSsidTrackingInfo(workerAndPreferencesVo, new SsidTrackingInfoVo(workerExternalId, newSsidTrackingReportDto.getSsid(), timeMachineService.getNow()));

        LOG.debug("END newSsidTrackingReport");

        return result;
    }
}
