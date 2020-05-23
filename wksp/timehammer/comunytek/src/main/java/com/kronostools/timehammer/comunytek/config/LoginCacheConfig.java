package com.kronostools.timehammer.comunytek.config;

import com.kronostools.timehammer.common.config.AbstractCacheConfig;
import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "timehammer.comunytek.login-cache", namingStrategy= ConfigProperties.NamingStrategy.KEBAB_CASE)
public class LoginCacheConfig extends AbstractCacheConfig {
}