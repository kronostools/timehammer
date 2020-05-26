package com.kronostools.timehammer.commandprocessor.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.commandprocessor.config.ChatbotRegistrationRequestCacheConfig;
import com.kronostools.timehammer.commandprocessor.model.ChatbotRegistrationRequest;
import com.kronostools.timehammer.common.services.TimeMachineService;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class RegistrationRequestService {
    private final Cache<String, ChatbotRegistrationRequest> chatbotRegistrationRequestCache;
    private final TimeMachineService timeMachineService;

    public RegistrationRequestService(final ChatbotRegistrationRequestCacheConfig chatbotRegistrationRequestCacheConfig,
                                      final TimeMachineService timeMachineService) {
        this.chatbotRegistrationRequestCache = Caffeine.newBuilder()
                .expireAfterWrite(chatbotRegistrationRequestCacheConfig.getExpiration().getQty(), chatbotRegistrationRequestCacheConfig.getExpiration().getUnit())
                .build();

        this.timeMachineService = timeMachineService;
    }

    public ChatbotRegistrationRequest newRegistrationRequest(final String chatId) {
        final String newWorkerInternalId = UUID.randomUUID().toString();
        final ChatbotRegistrationRequest chatbotRegistrationRequest = new ChatbotRegistrationRequest(newWorkerInternalId, chatId, timeMachineService.getNow());

        chatbotRegistrationRequestCache.put(newWorkerInternalId, chatbotRegistrationRequest);

        return chatbotRegistrationRequest;
    }

    public ChatbotRegistrationRequest getRegistrationRequest(final String workerInternalId) {
        return chatbotRegistrationRequestCache.getIfPresent(workerInternalId);
    }
}