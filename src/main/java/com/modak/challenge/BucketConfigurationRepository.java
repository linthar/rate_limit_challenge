package com.modak.challenge;

public interface BucketConfigurationRepository {

    void addConfiguration(String type, BucketConfiguration configuration);
    BucketConfiguration getConfiguration(String type);


}