package com.kronostools.timehammer.web.model;

public class StreamSubscriberBuilder {
    private String subscriberId;

    public StreamSubscriberBuilder subscriberId(final String subscriberId) {
        this.subscriberId = subscriberId;
        return this;
    }

    public StreamSubscriber build() {
        final StreamSubscriber result = new StreamSubscriber();
        result.setSubscriberId(subscriberId);

        return result;
    }
}