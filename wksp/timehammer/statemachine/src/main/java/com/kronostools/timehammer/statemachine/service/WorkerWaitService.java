package com.kronostools.timehammer.statemachine.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.constants.WorkerStatusAction;
import com.kronostools.timehammer.common.messages.constants.AnswerOption;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.statemachine.model.Wait;
import com.kronostools.timehammer.statemachine.model.WaitId;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@ApplicationScoped
public class WorkerWaitService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerWaitService.class);

    private static final String CACHE_TMP_FILE = "/data/cache.ser";

    private final Cache<WaitId, Wait> workerWaitsCache;
    private final TimeMachineService timeMachineService;

    public WorkerWaitService(final TimeMachineService timeMachineService) {
        this.timeMachineService = timeMachineService;

        this.workerWaitsCache = Caffeine.newBuilder().build();
    }

    public boolean workerHasWaitForQuestionAt(final String workerInternalId, final WorkerStatusAction statusAction, final LocalDateTime timestamp) {
        final WaitId waitId = new WaitId(workerInternalId, statusAction);

        final Wait foundWait = workerWaitsCache.getIfPresent(waitId);

        final boolean existingWait;

        if (foundWait == null) {
            LOG.debug("Worker '{}' has no wait for '{}'", workerInternalId, statusAction.name());

            existingWait = false;
        } else if (foundWait.isExpired(timestamp)) {
            LOG.debug("Worker '{}' has an expired wait for '{}', invalidating it ...", workerInternalId, statusAction.name());

            workerWaitsCache.invalidate(waitId);
            existingWait = false;
        } else {
            if (foundWait.isAllDay()) {
                LOG.debug("Worker '{}' has all day wait for '{}'", workerInternalId, statusAction.name());
            } else {
                LOG.debug("Worker '{}' has a wait for '{}' until '{}'", workerInternalId, statusAction.name(), CommonDateTimeUtils.formatDateTimeToLog(foundWait.getLimitTimestamp()));
            }

            existingWait = true;
        }

        return existingWait;
    }

    public boolean workerHasAllDayWaitForAction(final String workerInternalId, final WorkerStatusAction statusAction, final LocalDateTime timestamp) {
        final WaitId waitId = new WaitId(workerInternalId, statusAction);

        final Wait foundWait = workerWaitsCache.getIfPresent(waitId);

        final boolean allDayWait;

        if (foundWait == null) {
            LOG.debug("Worker '{}' has no wait for question '{}'", workerInternalId, statusAction.name());

            allDayWait = false;
        } else if (foundWait.isExpired(timestamp)) {
            LOG.debug("Worker '{}' has an expired all day wait for '{}', invalidating it ...", workerInternalId, statusAction.name());

            workerWaitsCache.invalidate(waitId);
            allDayWait = false;
        } else {
            allDayWait = foundWait.isAllDay();

            if (allDayWait) {
                LOG.debug("Worker '{}' has all day wait for '{}'", workerInternalId, statusAction.name());
            } else {
                LOG.debug("Worker '{}' has a wait for '{}' until '{}'", workerInternalId, statusAction.name(), CommonDateTimeUtils.formatDateTimeToLog(foundWait.getLimitTimestamp()));
            }
        }

        return allDayWait;
    }

    public void saveWaitForWorkerAndQuestion(final String workerInternalId, final AnswerOption answerOption) {
        if (answerOption.isWait()) {
            final LocalDateTime waitLimitTimestamp = answerOption.getWaitLimitTimestamp(timeMachineService.getNow());

            if (waitLimitTimestamp != null) {
                workerWaitsCache.put(new WaitId(workerInternalId, answerOption), new Wait(waitLimitTimestamp, answerOption.isAllDayWait()));
            } else {
                LOG.warn("Wait limit timestamp could not be determined for worker '{}' and answer '{}'", workerInternalId, answerOption.getCode());
            }
        }
    }

    public void dumpWaits() {
        final ConcurrentMap<WaitId, Wait> source = workerWaitsCache.asMap();

        if (source.isEmpty()) {
            LOG.debug("No waits to dump");
        } else {
            final LocalDateTime timestamp = timeMachineService.getNow();

            LOG.debug("Dumping unexpired waits at '{}' to temporary file ...", CommonDateTimeUtils.formatDateTimeToLog(timestamp));

            final HashMap<WaitId, Wait> m = new HashMap<>(source.size());
            source.forEach((key, value) -> {
                if (!value.isExpired(timestamp)) {
                    m.put(key, value);
                }
            });

            try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                SerializationUtils.serialize(m, baos);
                FileUtils.writeByteArrayToFile(new File(CACHE_TMP_FILE), baos.toByteArray());
                LOG.debug("Dumped {} waits to temporary file!", m.size());
            } catch (IOException e) {
                LOG.error("Impossible to dump waits cache to temporary file", e);
            }
        }
    }

    public void loadWaits() {
        final File cacheFile = new File(CACHE_TMP_FILE);

        if (cacheFile.exists() && cacheFile.canRead()) {
            LOG.debug("Loading credentials from temporary file ...");

            try (ByteArrayInputStream bais = new ByteArrayInputStream(FileUtils.readFileToByteArray(cacheFile)); ObjectInputStream ois = new ObjectInputStream(bais)) {
                final Map<WaitId, Wait> m = (Map<WaitId, Wait>) ois.readObject();
                workerWaitsCache.putAll(m);

                LOG.debug("Loaded {} waits from temprary file!", m.size());

                if (cacheFile.canWrite()) {
                    LOG.debug("Deleting temporary file ...");
                    if (cacheFile.delete()) {
                        LOG.debug("Deleted temporary file!");
                    } else {
                        LOG.warn("Temporary file could not be deleted");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                LOG.error("Impossible to load waits cache from temporary file", e);
            }
        } else {
            LOG.info("There is no temporary file (or cannot be read) to load waits cache");
        }
    }
}
