package com.kronostools.timehammer.comunytek.config;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(
        classNames = {
                "com.github.benmanes.caffeine.cache.SSW",
                "com.github.benmanes.caffeine.cache.PSW",
                "com.kronostools.timehammer.comunytek.model.CachedWorkerCredentials"
        })
public class ClassesRegisteredForReflection {
}