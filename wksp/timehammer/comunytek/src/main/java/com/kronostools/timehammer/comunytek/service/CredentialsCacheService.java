package com.kronostools.timehammer.comunytek.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.comunytek.model.CachedWorkerCredentials;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@ApplicationScoped
public class CredentialsCacheService {
    private static final Logger LOG = LoggerFactory.getLogger(CredentialsCacheService.class);

    private static final String CACHE_TMP_FILE = "/data/cache.ser";

    private final ObjectMapper om;
    private final Cache<String, CachedWorkerCredentials> credentialsCache;

    public CredentialsCacheService() {
        this.om = new ObjectMapper();
        this.om.registerModule(new JavaTimeModule());

        this.credentialsCache = Caffeine.newBuilder()
                .build();
    }

    public CachedWorkerCredentials getCredentials(final String username) {
        return credentialsCache.getIfPresent(username);
    }

    public void updateCredentials(final String username, final CachedWorkerCredentials credentials) {
        credentialsCache.put(username, credentials);
    }

    public void dumpCredentials() {
        final ConcurrentMap<String, CachedWorkerCredentials> source = credentialsCache.asMap();

        if (source.isEmpty()) {
            LOG.info("No credentials to dump");
        } else {
            LOG.info("Dumping credentials to temporary file ...");

            final HashMap<String, CachedWorkerCredentials> m = new HashMap<>(source.size());
            m.putAll(source);

            try {
                FileUtils.writeByteArrayToFile(new File(CACHE_TMP_FILE), om.writeValueAsBytes(m));
                LOG.info("Dumped {} credentials to temporary file!", m.size());
            } catch (IOException e) {
                LOG.error("Impossible to dump credentials cache to temporary file", e);
            }
        }
    }

    public void loadCredentials() {
        final File cacheFile = new File(CACHE_TMP_FILE);

        if (cacheFile.exists() && cacheFile.canRead()) {
            LOG.info("Loading credentials from temporary file ...");

            try (ByteArrayInputStream bais = new ByteArrayInputStream(FileUtils.readFileToByteArray(cacheFile))) {
                TypeReference<Map<String, CachedWorkerCredentials>> typeRef = new TypeReference<>() {};
                final Map<String, CachedWorkerCredentials> m = om.readValue(bais, typeRef);
                credentialsCache.putAll(m);

                LOG.info("Loaded {} credentials from temprary file!", m.size());

                if (cacheFile.canWrite()) {
                    LOG.info("Deleting temporary file ...");
                    if (cacheFile.delete()) {
                        LOG.info("Deleted temporary file!");
                    } else {
                        LOG.warn("Temporary file could not be deleted");
                    }
                }
            } catch (IOException e) {
                LOG.error("Impossible to load credentials cache from temporary file", e);
            }
        } else {
            LOG.info("There is no temporary file (or cannot be read) to load credentials cache");
        }
    }
}