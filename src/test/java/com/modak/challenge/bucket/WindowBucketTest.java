package com.modak.challenge.bucket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WindowBucketTest {


    @Test
    void addNotification() {

        // slice window : max 5 notifications / 30 minutes (=1800 secs)
        WindowBucket windowBucket = new WindowBucket(1800, 5);

        // only 5 notifications must be added
        windowBucket.addNotification();
        windowBucket.addNotification();
        windowBucket.addNotification();
        windowBucket.addNotification();
        windowBucket.addNotification();
        // new notifications will be rejected
        // because 30 minutes window range


        /// TEST
        /////////////////////////

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> windowBucket.addNotification(),
                "Should throw RuntimeException");

        // ASSERT
        /////////////////////////
        assertEquals(WindowBucket.WINDOW_BUCKET_IS_FULL_MESSAGE, thrown.getMessage());

    }


    /// INNER METHODS TESTS
    // see also RemoveExpiredNotificationsBucketTest tests


    @Test
    void add() {
        // slice window : max 10 notifications / 10 minute (=600 secs)
        WindowBucket windowBucket = new WindowBucket(600, 10);

        assertEquals(0, windowBucket.timestampsList.size());
        /// TEST
        /////////////////////////

        long nowInSeconds = System.currentTimeMillis() / 1000;

        Long tsAdded1 = nowInSeconds - 10;
        windowBucket.add(tsAdded1);

        Long tsAdded2 = nowInSeconds - 5;
        windowBucket.add(tsAdded2);

        Long tsAdded3 = nowInSeconds - 1;
        windowBucket.add(tsAdded3);

        // ASSERT
        /////////////////////////
        // check that ts were added ordered
        assertEquals(3, windowBucket.timestampsList.size());
        // test the timestamps list
        assertEquals(tsAdded1, windowBucket.timestampsList.getFirst());
        assertEquals(tsAdded2, windowBucket.timestampsList.get(1));
        assertEquals(tsAdded3, windowBucket.timestampsList.getLast());
    }

}