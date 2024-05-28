package com.modak.challenge;

import java.util.HashMap;
import java.util.Map;

public class BucketConfigurationRepositoryImpl implements BucketConfigurationRepository {

    Map<String, BucketConfiguration> typeConfigurationsStorage = new HashMap<>();

    public void addConfiguration(String type, BucketConfiguration configuration){
        typeConfigurationsStorage.put(type, configuration);
    }

    public BucketConfiguration getConfiguration(String type){
        return typeConfigurationsStorage.get(type);
    }


}