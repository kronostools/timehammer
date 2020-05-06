package com.kronostools.timehammer.integration;

import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class Lifecycle {
    private static final Logger LOG = LoggerFactory.getLogger(Lifecycle.class);

    private final TimeMachineService timeMachineService;

    public Lifecycle(final TimeMachineService timeMachineService) {
        this.timeMachineService = timeMachineService;
    }

    void onStartup(@Observes StartupEvent event) {
        LOG.info("BEGIN onStartup");

        final List<String> properties = new ArrayList<>();

        LOG.info("Timestamp: {}", CommonDateTimeUtils.formatDateTimeToLog(timeMachineService.getNow()));

        ConfigProvider.getConfig()
                .getConfigSources()
                .forEach(cs -> LOG.info("Config source: {}", cs.getName()));

        ConfigProvider.getConfig()
                .getPropertyNames()
                .forEach(pn -> {
                    if (pn.startsWith("mp.") || pn.startsWith("timehammer.") || pn.startsWith("kafka.") || pn.startsWith("quarkus.")) {
                        properties.add(pn);
                    }
                });

        properties.forEach(pn -> {
            LOG.info("{}: {}", pn, ConfigProvider.getConfig().getValue(pn, String.class));
        });

        LOG.info("END onStartup");
    }
}