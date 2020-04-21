package com.kronostools.timehammer.service;

import com.kronostools.timehammer.dao.SsidTrackingEventDao;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@ApplicationScoped
public class WorkerSsidTrackingEventService {
    private final SsidTrackingEventDao ssidTrackingEventDao;

    public WorkerSsidTrackingEventService(final SsidTrackingEventDao ssidTrackingEventDao) {
        this.ssidTrackingEventDao = ssidTrackingEventDao;
    }

    @Transactional
    public void cleanSsidTrackingEventsUntil(final LocalDateTime timestamp) {
        ssidTrackingEventDao.cleanSsidTrackingEventsUntil(timestamp);
    }
}
