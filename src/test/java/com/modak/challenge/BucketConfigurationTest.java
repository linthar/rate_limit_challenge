package com.modak.challenge;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class BucketConfigurationTest {

    @Test
    void timesliceSeconds() {
        BucketConfiguration conf = new BucketConfiguration(60, 5);
        assertEquals(60, conf.timesliceSeconds());
    }

    @Test
    void bucketSize() {
        BucketConfiguration conf = new BucketConfiguration(60, 10);
        assertEquals(10, conf.bucketSize());
    }
}