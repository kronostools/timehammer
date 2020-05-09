package com.kronostools.timehammer.comunytek;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidaysWorker;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HolidaysWorkerRetriever {
    private static final Logger LOG = LoggerFactory.getLogger(HolidaysWorkerRetriever.class);

    private final ComunytekClient comunytekClient;

    public HolidaysWorkerRetriever(final ComunytekClient comunytekClient) {
        this.comunytekClient = comunytekClient;
    }

    @Incoming(Channels.COMUNYTEK_WORKER_HOLIDAYS)
    //@Outgoing(Channels.HOLIDAYS_WORKER_UPDATE)
    //public Uni<Message<UpdateWorkersHolidaysWorker>> routeHolidays(final Message<UpdateWorkersHolidaysWorker> message) {
    public void routeHolidays(final UpdateWorkersHolidaysWorker worker) {
        LOG.info("Getting holidays of worker '{}' from company '{}'", worker.getWorkerInternalId(), worker.getCompany().getCode());

        LOG.debug("Comunytek client mocked: {}", comunytekClient.isMocked());
    }
}