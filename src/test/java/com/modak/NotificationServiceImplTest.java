package com.modak;

import com.modak.challenge.bucket.BucketsMap;
import com.modak.challenge.bucket.BucketsMapImpl;
import com.modak.challenge.bucket.WindowBucket;
import com.modak.challenge.configuration.RateLimitConfigurationRepository;
import com.modak.challenge.exception.RateLimitExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    private NotificationServiceImpl notificationService;
    private Gateway gatewayMock;
    private BucketsMap bucketsMapMock;
    private ThreadLocalRandom random;
    private WindowBucket windowBucketMock;

    @BeforeEach
    void setUp() {
        bucketsMapMock = mock(BucketsMap.class);
        gatewayMock = mock(Gateway.class);
        notificationService = new NotificationServiceImpl(gatewayMock, bucketsMapMock);
        windowBucketMock = mock(WindowBucket.class);
        random = ThreadLocalRandom.current();

    }

    @Test
    void send() {
        // create random type name and userId
        String type = "name_" + random.nextInt();
        String userId = "user_" + random.nextInt();
        String message = "some Message_" + random.nextInt();
        when(bucketsMapMock.getWindowBucket(type, userId)).thenReturn(windowBucketMock);

        /// TEST
        /////////////////////////
        notificationService.send(type, userId, message);

        // ASSERT
        /////////////////////////
        // check that bucket availability was checked
        verify(windowBucketMock).addNotification();
        // asserts message was sent to gateway
        verify(gatewayMock).send(userId, message);

    }

    @Test
    void send_WhenBucketIsFull() {
        // create random type name and userId
        String type = "name_" + random.nextInt();
        String userId = "user_" + random.nextInt();
        String message = "some Message_" + random.nextInt();

        // setup bucket to fail (bucket is full)
        when(bucketsMapMock.getWindowBucket(type, userId)).thenReturn(windowBucketMock);
        doThrow(new RateLimitExceededException("some error")).when(windowBucketMock).addNotification();


        /// TEST
        /////////////////////////
        notificationService.send(type, userId, message);

        // ASSERT
        /////////////////////////
        // asserts message was NOT sent to gateway
        verifyNoInteractions(gatewayMock);

    }

}