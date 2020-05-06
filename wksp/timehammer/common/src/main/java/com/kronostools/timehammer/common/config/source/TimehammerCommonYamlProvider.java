package com.kronostools.timehammer.common.config.source;

public class TimehammerCommonYamlProvider extends TimehammerYamlProvider {
    public static final String YAML = "timehammer_common.yaml";
    private static final int YAML_IN_JAR_ORDINAL = 252;

    @Override
    String yamlPath() {
        return YAML;
    }

    @Override
    int yamlInJarOrdinal() {
        return YAML_IN_JAR_ORDINAL;
    }
}