package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.model.WorkerSsidTrackingEvent;
import com.kronostools.timehammer.model.WorkerSsidTrackingEventId;
import com.kronostools.timehammer.utils.Constants;
import com.kronostools.timehammer.vo.SsidTrackingEventVo;
import io.quarkus.vertx.ConsumeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class SsidTrackingEventProcessorDao {
    private static final Logger LOG = LoggerFactory.getLogger(SsidTrackingEventProcessorDao.class);

    private final SsidTrackingEventDao ssidTrackingEventDao;

    public SsidTrackingEventProcessorDao(final SsidTrackingEventDao ssidTrackingEventDao) {
        this.ssidTrackingEventDao = ssidTrackingEventDao;
    }

    @ConsumeEvent(value = Constants.Buses.ADD_SSID_TRACKING_EVENT, blocking = true)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void addSsidTrackingEventProcessor(final SsidTrackingEventVo newSsidTrackingEventVo) {
        LOG.debug("BEGIN addSsidTrackingEventProcessor: [{}]", newSsidTrackingEventVo);

        WorkerSsidTrackingEventId newWorkerSsidTrackingEventId = new WorkerSsidTrackingEventId();
        newWorkerSsidTrackingEventId.setWorkerExternalId(newSsidTrackingEventVo.getWorkerExternalId());
        newWorkerSsidTrackingEventId.setEventType(newSsidTrackingEventVo.getEventType());
        newWorkerSsidTrackingEventId.setOccurred(newSsidTrackingEventVo.getOccurred());

        WorkerSsidTrackingEvent newWorkerSsidTrackingEvent = new WorkerSsidTrackingEvent();
        newWorkerSsidTrackingEvent.setId(newWorkerSsidTrackingEventId);

        ssidTrackingEventDao.save(newWorkerSsidTrackingEvent);

        LOG.debug("END addSsidTrackingEventProcessor");
    }
}