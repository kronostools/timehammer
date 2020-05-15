package com.kronostools.timehammer.web.model;

import com.kronostools.timehammer.web.dto.Dto;
import io.smallrye.mutiny.subscription.MultiEmitter;

public class StreamSubscriber {
    private String subscriberId;
    private MultiEmitter<? super Dto> emitter;

    StreamSubscriber() {}

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public MultiEmitter<? super Dto> getEmitter() {
        return emitter;
    }

    public void setEmitter(MultiEmitter<? super Dto> emitter) {
        this.emitter = emitter;
    }
}