package com.kronostools.timehammer.common.config.source;

public class TimehammerTimemachineYamlProvider extends TimehammerYamlProvider {
    public static final String YAML = "timehammer_timemachine.yaml";
    private static final int YAML_IN_JAR_ORDINAL = 251;


    @Override
    String yamlPath() {
        return YAML;
    }

    @Override
    int yamlInJarOrdinal() {
        return YAML_IN_JAR_ORDINAL;
    }
}