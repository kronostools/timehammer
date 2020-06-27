package com.kronostools.timehammer.comunytek.client;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.comunytek.model.CachedWorkerCredentials;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractComunytekClient implements ComunytekClient {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static final String CACHE_TMP_FILE = "/data/cache.ser";

    protected final Cache<String, CachedWorkerCredentials> credentialsCache;

    public AbstractComunytekClient() {
        this.credentialsCache = Caffeine.newBuilder()
                .build();
    }

    @Override
    public void dumpCredentials() {
        final ConcurrentMap<String, CachedWorkerCredentials> source = credentialsCache.asMap();

        if (source.isEmpty()) {
            LOG.debug("No credentials to dump");
        } else {
            LOG.debug("Dumping credentials to temporary file ...");

            final HashMap<String, CachedWorkerCredentials> m = new HashMap<>(source.size());
            m.putAll(source);


            try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                SerializationUtils.serialize(m, baos);
                FileUtils.writeByteArrayToFile(new File(CACHE_TMP_FILE), baos.toByteArray());
                LOG.debug("Dumped {} credentials to temporary file!", m.size());
            } catch (IOException e) {
                LOG.error("Impossible to dump credentials cache to temporary file", e);
            }
        }
    }

    @Override
    public void loadCredentials() {
        final File cacheFile = new File(CACHE_TMP_FILE);

        if (cacheFile.exists() && cacheFile.canRead()) {
            LOG.debug("Loading credentials from temporary file ...");

            try (ByteArrayInputStream bais = new ByteArrayInputStream(FileUtils.readFileToByteArray(cacheFile)); ObjectInputStream ois = new ObjectInputStream(bais)) {
                final Map<String, CachedWorkerCredentials> m = (Map<String, CachedWorkerCredentials>) ois.readObject();
                credentialsCache.putAll(m);

                LOG.debug("Loaded {} credentials from temprary file!", m.size());

                if (cacheFile.canWrite()) {
                    LOG.debug("Deleting temporary file ...");
                    if (cacheFile.delete()) {
                        LOG.debug("Deleted temporary file!");
                    } else {
                        LOG.warn("Temporary file could not be deleted");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                LOG.error("Impossible to load credentials cache from temporary file", e);
            }
        } else {
            LOG.info("There is no temporary file (or cannot be read) to load credentials cache");
        }
    }
}
