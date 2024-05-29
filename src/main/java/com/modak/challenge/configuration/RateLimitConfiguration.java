package com.modak.challenge.configuration;

public record RateLimitConfiguration(
        long timeInSeconds,
        int notificationsLimitCount) {
}
