package com.modak.challenge.bucket;

import com.modak.challenge.configuration.RateLimitConfiguration;
import com.modak.challenge.configuration.RateLimitConfigurationRepository;

import java.util.HashMap;
import java.util.Map;

public class BucketsMapImpl implements BucketsMap {

    protected Map<String, WindowBucket> windowBucketsMap;
    protected RateLimitConfigurationRepository configurationRepository;

    public BucketsMapImpl(RateLimitConfigurationRepository configurationRepository) {
        this.windowBucketsMap = new HashMap<>();
        this.configurationRepository = configurationRepository;
    }

    @Override
    public WindowBucket getWindowBucket(String type, String userId) {

        String key = getKey(type, userId);
        WindowBucket bucket = windowBucketsMap.get(key);

        if (bucket == null) {
            // if not found....
            // create a new one for this type/userId
            bucket = createBucket(type);
            windowBucketsMap.put(key, bucket);
        }

        return bucket;
    }

    /**
     * Creates a new WindowBucket configured for the given type
     *
     * @param type the notification type to configure the WindowBucket
     * @return the WindowBucket for the type
     */
    private WindowBucket createBucket(String type) {
        RateLimitConfiguration config = configurationRepository.getConfiguration(type);
        return new WindowBucket(config.timeInSeconds(), config.notificationsLimitCount());
    }


    /**
     * Builds the windowBucketsMap key for the given type & userId WindowBucket
     *
     * @param type   the type to build the key
     * @param userId the type to build the key
     * @return type & userId WindowBucket map key
     */
    protected String getKey(String type, String userId) {
        return type + "_" + userId;
    }

}
