package com.kronostools.timehammer.common.config.source;

import io.smallrye.config.source.yaml.YamlConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class TimehammerYamlProvider implements ConfigSourceProvider {
    public static final String TIMEHAMMER_YAML = "timehammer.yaml";
    private static final int TIMEHAMMER_YAML_IN_JAR_ORDINAL = 252;

    @Override
    public Iterable<ConfigSource> getConfigSources(final ClassLoader forClassLoader) {
        return getConfigSourcesForFileName(TIMEHAMMER_YAML, TIMEHAMMER_YAML_IN_JAR_ORDINAL, forClassLoader);
    }

    private List<ConfigSource> getConfigSourcesForFileName(final String fileName, final int inJarOrdinal, final ClassLoader forClassLoader) {
        List<ConfigSource> sources = Collections.emptyList();

        // mirror the in-JAR application.properties
        try {
            InputStream str = forClassLoader.getResourceAsStream(fileName);
            if (str != null) {
                try (Closeable c = str) {
                    YamlConfigSource configSource = new YamlConfigSource(fileName, str, inJarOrdinal);
                    sources = Collections.singletonList(configSource);
                }
            }
        } catch (IOException e) {
            // configuration problem should be thrown
            throw new IOError(e);
        }

        // mirror the on-filesystem application.properties
        final Path path = Paths.get("config", fileName);

        if (Files.exists(path)) {
            try (InputStream str = Files.newInputStream(path)) {
                YamlConfigSource configSource = new YamlConfigSource(fileName, str, inJarOrdinal + 10);

                if (sources.isEmpty()) {
                    sources = Collections.singletonList(configSource);
                } else {
                    sources = List.of(sources.get(0), configSource);
                }
            } catch (NoSuchFileException | FileNotFoundException e) {
                // skip (race)
            } catch (IOException e) {
                // configuration problem should be thrown
                throw new IOError(e);
            }
        }

        return sources;
    }
}