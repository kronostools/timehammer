package com.kronostools.timehammer.statemachine.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.messages.constants.QuestionType;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.statemachine.model.Wait;
import com.kronostools.timehammer.statemachine.model.WaitId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class WorkerWaitService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerWaitService.class);

    private final Cache<WaitId, Wait> workerWaitsCache;

    public WorkerWaitService() {
        this.workerWaitsCache = Caffeine.newBuilder().build();
    }

    public boolean workerHasWaitForQuestionAt(final String workerInternalId, final QuestionType questionType, final LocalDateTime timestamp) {
        final WaitId waitId = new WaitId(workerInternalId, questionType);

        final Wait foundWait = workerWaitsCache.getIfPresent(waitId);

        final boolean existingWait;

        if (foundWait == null) {
            LOG.debug("Worker '{}' has no wait for question '{}'", workerInternalId, questionType.name());

            existingWait = false;
        } else if (foundWait.isExpired(timestamp)) {
            LOG.debug("Worker '{}' has an expired wait for question '{}', invalidating it ...", workerInternalId, questionType.name());

            workerWaitsCache.invalidate(waitId);
            existingWait = false;
        } else {
            LOG.debug("Worker '{}' has a wait for question '{}' until '{}'", workerInternalId, questionType.name(), CommonDateTimeUtils.formatDateTimeToLog(timestamp));

            existingWait = true;
        }

        return existingWait;
    }
}
