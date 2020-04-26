package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.model.WorkerSsidTrackingInfo;
import com.kronostools.timehammer.utils.Constants.Buses;
import com.kronostools.timehammer.vo.SsidTrackingInfoVo;
import io.quarkus.vertx.ConsumeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class WorkerSsidTrackingInfoProcessorDao {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerSsidTrackingInfoProcessorDao.class);

    private final WorkerSsidTrackingInfoDao workerSsidTrackingInfoDao;

    public WorkerSsidTrackingInfoProcessorDao(final WorkerSsidTrackingInfoDao workerSsidTrackingInfoDao) {
        this.workerSsidTrackingInfoDao = workerSsidTrackingInfoDao;
    }

    @ConsumeEvent(value = Buses.UPDATE_WORKER_SSID_TRACKING_INFO, blocking = true)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void updateWorkerSsidTrackingInfoProcessor(final SsidTrackingInfoVo newSsidTrackingInfoVo) {
        LOG.debug("BEGIN updateWorkerSsidTrackingInfoProcessor: [{}]", newSsidTrackingInfoVo);

        WorkerSsidTrackingInfo newWorkerSsidTrackingInfo = new WorkerSsidTrackingInfo();
        newWorkerSsidTrackingInfo.setWorkerInternalId(newSsidTrackingInfoVo.getWorkerInternalId());
        newWorkerSsidTrackingInfo.setSsidReported(newSsidTrackingInfoVo.getSsid());
        newWorkerSsidTrackingInfo.setReported(newSsidTrackingInfoVo.getReported());

        workerSsidTrackingInfoDao.update(newWorkerSsidTrackingInfo);

        LOG.debug("END updateWorkerSsidTrackingInfoProcessor");
    }
}