package com.kronostools.timehammer.manager;

import com.kronostools.timehammer.dao.WorkerSsidTrackingInfoDao;
import com.kronostools.timehammer.dto.SsidTrackingEventDto;
import com.kronostools.timehammer.enums.SsidTrackingEventType;
import com.kronostools.timehammer.model.WorkerSsidTrackingInfo;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.utils.Constants;
import com.kronostools.timehammer.utils.Constants.Buses;
import com.kronostools.timehammer.utils.Utils;
import com.kronostools.timehammer.vo.SsidTrackingEventVo;
import com.kronostools.timehammer.vo.SsidTrackingInfoVo;
import com.kronostools.timehammer.vo.WorkerPreferencesVo;
import io.vertx.axle.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerSsidTrackingInfoManager {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerSsidTrackingInfoManager.class);

    private final TimeMachineService timeMachineService;
    private final WorkerSsidTrackingInfoDao workerSsidTrackingInfoDao;
    private final EventBus bus;

    public WorkerSsidTrackingInfoManager(final TimeMachineService timeMachineService,
                                         final WorkerSsidTrackingInfoDao workerSsidTrackingInfoDao,
                                         final EventBus bus) {
        this.timeMachineService = timeMachineService;
        this.workerSsidTrackingInfoDao = workerSsidTrackingInfoDao;
        this.bus = bus;
    }

    public SsidTrackingEventDto updateWorkerSsidTrackingInfo(WorkerPreferencesVo workerPreferencesVo, final SsidTrackingInfoVo newSsidTrackingInfoVo) {
        LOG.debug("BEGIN updateWorkerSsidTrackingInfo: [{}] [{}]", newSsidTrackingInfoVo, workerPreferencesVo);

        final SsidTrackingInfoVo lastSsidTrackingInfoVo = workerSsidTrackingInfoDao.fetchByWorkerExternalId(workerPreferencesVo.getWorkerExternalId());

        LOG.debug("Last ssid tracking info of worker [{}]", lastSsidTrackingInfoVo);

        workerSsidTrackingInfoDao.updateWorkerSsidTrackingInfo(newSsidTrackingInfoVo);

        final String workSsid = workerPreferencesVo.getWorkSsid();

        final SsidTrackingEventType ssidTrackingEventType = Utils.getSsidTrackingEventType(lastSsidTrackingInfoVo.getSsid(), newSsidTrackingInfoVo.getSsid(), workSsid);

        LOG.debug("Generated ssid tracking event: {}", ssidTrackingEventType);

        if (ssidTrackingEventType != SsidTrackingEventType.NONE) {
            final SsidTrackingEventVo newSsidTrackingEvent = new SsidTrackingEventVo(workerPreferencesVo.getWorkerExternalId(), ssidTrackingEventType, newSsidTrackingInfoVo.getReported());

            bus.publish(Buses.ADD_SSID_TRACKING_EVENT, newSsidTrackingEvent);

            LOG.debug("Emited event [{}] in bus '{}'", newSsidTrackingEvent, Buses.ADD_SSID_TRACKING_EVENT);
        }

        LOG.debug("END updateWorkerSsidTrackingInfo");

        return new SsidTrackingEventDto(workerPreferencesVo.getWorkerExternalId(), ssidTrackingEventType, newSsidTrackingInfoVo.getReported());
    }

    public void initializeSsidTrackingInfo(final String workerExternalId) {
        WorkerSsidTrackingInfo workerSsidTrackingInfo = new WorkerSsidTrackingInfo();
        workerSsidTrackingInfo.setWorkerExternalId(workerExternalId);
        workerSsidTrackingInfo.setSsidReported(Constants.NO_SSID);
        workerSsidTrackingInfo.setReported(timeMachineService.getNow());

        workerSsidTrackingInfoDao.initializeSsidTrackingInfo(workerSsidTrackingInfo);
    }
}