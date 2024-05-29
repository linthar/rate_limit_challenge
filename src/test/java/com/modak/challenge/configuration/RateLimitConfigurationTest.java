package com.modak.challenge.configuration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class RateLimitConfigurationTest {

    @Test
    void timesliceSeconds() {
        RateLimitConfiguration conf = new RateLimitConfiguration(60, 5);
        assertEquals(60, conf.timeInSeconds());
    }

    @Test
    void bucketSize() {
        RateLimitConfiguration conf = new RateLimitConfiguration(60, 10);
        assertEquals(10, conf.notificationsLimitCount());
    }
}