package com.kronostools.timehammer.common.processors;

import com.kronostools.timehammer.common.constants.CommonConstants;
import com.kronostools.timehammer.common.messages.timemachine.TimeMachineEventMessage;
import com.kronostools.timehammer.common.services.TimeMachineService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

public class TimeMachineProcessor {
    private final TimeMachineService timeMachineService;

    public TimeMachineProcessor(final TimeMachineService timeMachineService) {
        this.timeMachineService = timeMachineService;
    }

    @Incoming(CommonConstants.Channels.TIMEMACHINE)
    public CompletionStage<Void> process(final Message<TimeMachineEventMessage> message) {
        final TimeMachineEventMessage timeMachineEvent = message.getPayload();

        timeMachineService.timeTravelToDateTimeWithZone(timeMachineEvent.getNewTimestamp(), timeMachineEvent.getTimezone());

        return message.ack();
    }
}