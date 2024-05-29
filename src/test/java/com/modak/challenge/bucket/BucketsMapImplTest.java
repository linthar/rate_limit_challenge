package com.modak.challenge.bucket;

import com.modak.challenge.configuration.RateLimitConfiguration;
import com.modak.challenge.configuration.RateLimitConfigurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BucketsMapImplTest {

    private BucketsMapImpl bucketsMap;
    private RateLimitConfigurationRepository rateLimitConfigurationRepositoryMock;
    private ThreadLocalRandom random;

    @BeforeEach
    void setUp() {
        rateLimitConfigurationRepositoryMock = mock(RateLimitConfigurationRepository.class);
        bucketsMap = new BucketsMapImpl(rateLimitConfigurationRepositoryMock);
        random = ThreadLocalRandom.current();
    }

    @Test
    void getWindowBucket_WhenThereIsNoBucketForUserAndType() {

        // create random type name and userId
        String type = "name_" + random.nextInt();
        String userId = "user_" + random.nextInt();

        // setup config repository mock
        RateLimitConfiguration config = new RateLimitConfiguration(random.nextLong(), random.nextInt());
        when(rateLimitConfigurationRepositoryMock.getConfiguration(type)).thenReturn(config);

        // verify that the map is empty
        assertEquals(0, bucketsMap.windowBucketsMap.size());

        /// TEST
        /////////////////////////
        WindowBucket bucket = bucketsMap.getWindowBucket(type, userId);

        // ASSERT
        /////////////////////////
        assertEquals(config.timeInSeconds(), bucket.timesliceSeconds);
        assertEquals(config.notificationsLimitCount(), bucket.bucketSize);
    }


    @Test
    void getWindowBucket_WhenBucketExists() {

        // create random type name and userId
        String type = "name_" + random.nextInt();
        String userId = "user_" + random.nextInt();

        // put a bucket mock for the type/userId
        String bucketsKey = bucketsMap.getKey(type, userId);
        WindowBucket bucketMock = mock(WindowBucket.class);
        bucketsMap.windowBucketsMap.put(bucketsKey, bucketMock);

        /// TEST
        /////////////////////////

        WindowBucket foundBucket = bucketsMap.getWindowBucket(type, userId);

        // ASSERT
        /////////////////////////
        assertEquals(bucketMock, foundBucket);

        // assert repository was not used
        verifyNoInteractions(rateLimitConfigurationRepositoryMock);
    }

}
