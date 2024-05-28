package com.modak.challenge;

public record BucketConfiguration(
        long timesliceSeconds,
        int bucketSize) {
}
