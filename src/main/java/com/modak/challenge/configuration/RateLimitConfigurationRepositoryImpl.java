package com.modak.challenge.configuration;

import java.util.HashMap;
import java.util.Map;

public class RateLimitConfigurationRepositoryImpl implements RateLimitConfigurationRepository {

    //should be replaced by a Database table in a PROD env
    private Map<String, RateLimitConfiguration> typeConfigurationsStorage = new HashMap<>();
    // default rate-limit configuration
    protected static RateLimitConfiguration DEFAULT_CONFIGURATION = new RateLimitConfiguration(60, 10);

    public void addConfiguration(String type, long timeInSeconds, int notificationsLimitCount) {
        RateLimitConfiguration configuration = new RateLimitConfiguration(timeInSeconds, notificationsLimitCount);
        typeConfigurationsStorage.put(type, configuration);
    }

    public RateLimitConfiguration getConfiguration(String type) {
        return typeConfigurationsStorage.getOrDefault(type, DEFAULT_CONFIGURATION);
    }


}