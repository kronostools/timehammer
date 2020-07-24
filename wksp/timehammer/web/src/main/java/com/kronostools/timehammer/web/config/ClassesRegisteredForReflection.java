package com.kronostools.timehammer.web.config;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(classNames = {
        "com.github.benmanes.caffeine.cache.SSW",
        "com.github.benmanes.caffeine.cache.PSW"
})
public class ClassesRegisteredForReflection {
}