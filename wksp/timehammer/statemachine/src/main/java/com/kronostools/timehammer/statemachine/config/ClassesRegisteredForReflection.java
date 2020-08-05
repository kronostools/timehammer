package com.kronostools.timehammer.statemachine.config;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(
        classNames = {
                "com.github.benmanes.caffeine.cache.SSW",
                "com.github.benmanes.caffeine.cache.PSW",
                "com.kronostools.timehammer.statemachine.model.Wait"
        })
public class ClassesRegisteredForReflection {
}