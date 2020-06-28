package com.kronostools.timehammer.commandprocessor.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.commandprocessor.config.ChatbotUpdatePasswordRequestCacheConfig;
import com.kronostools.timehammer.commandprocessor.model.ChatbotUpdatePasswordRequest;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.services.TimeMachineService;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class UpdatePasswordRequestService {
    private final Cache<String, ChatbotUpdatePasswordRequest> chatbotUpdatePasswordRequestCache;
    private final TimeMachineService timeMachineService;

    public UpdatePasswordRequestService(final ChatbotUpdatePasswordRequestCacheConfig chatbotUpdatePasswordRequestCacheConfig,
                                        final TimeMachineService timeMachineService) {
        this.chatbotUpdatePasswordRequestCache = Caffeine.newBuilder()
                .expireAfterWrite(chatbotUpdatePasswordRequestCacheConfig.getExpiration().getQty(), chatbotUpdatePasswordRequestCacheConfig.getExpiration().getUnit())
                .build();

        this.timeMachineService = timeMachineService;
    }

    public String newUpdatePasswordRequest(final String workerInternalId, final String chatId, final Company company, final String workerExternalId) {
        final String requestId = UUID.randomUUID().toString();
        final ChatbotUpdatePasswordRequest chatbotUpdatePasswordRequest = new ChatbotUpdatePasswordRequest(workerInternalId, chatId, timeMachineService.getNow(), company, workerExternalId);

        chatbotUpdatePasswordRequestCache.put(requestId, chatbotUpdatePasswordRequest);

        return requestId;
    }

    public ChatbotUpdatePasswordRequest getUpdatePasswordRequest(final String requestId) {
        return chatbotUpdatePasswordRequestCache.getIfPresent(requestId);
    }
}