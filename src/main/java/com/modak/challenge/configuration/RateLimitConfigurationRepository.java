package com.modak.challenge.configuration;

public interface RateLimitConfigurationRepository {

    void addConfiguration(String type, long timeInSeconds, int notificationsLimitCount);

    RateLimitConfiguration getConfiguration(String type);

}