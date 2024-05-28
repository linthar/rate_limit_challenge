package com.modak.challenge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BucketTest {


    @Test
    void addNotification() {

        // slice window : max 5 notifications / 30 minutes (=1800 secs)
        Bucket bucket = new Bucket(1800, 5);

        // only 5 notifications must be added
        bucket.addNotification();
        bucket.addNotification();
        bucket.addNotification();
        bucket.addNotification();
        bucket.addNotification();
        // new notifications will be rejected
        // because 30 minutes window range


        /// TEST
        /////////////////////////

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> bucket.addNotification(),
                "Should throw RuntimeException");

        // ASSERT
        /////////////////////////
        assertEquals(Bucket.WINDOW_BUCKET_IS_FULL_MESSAGE, thrown.getMessage());

    }


    /// INNER METHODS TESTS
    // see also RemoveExpiredNotificationsBucketTest tests


    @Test
    void add() {
        // slice window : max 10 notifications / 10 minute (=600 secs)
        Bucket bucket = new Bucket(600, 10);

        assertEquals(0, bucket.timestampsList.size());
        /// TEST
        /////////////////////////

        long nowInSeconds = System.currentTimeMillis() / 1000;

        Long tsAdded1 = nowInSeconds-10;
        bucket.add(tsAdded1);

        Long tsAdded2 = nowInSeconds-5;
        bucket.add(tsAdded2);

        Long tsAdded3 = nowInSeconds-1;
        bucket.add(tsAdded3);

        // ASSERT
        /////////////////////////
        // check that ts were added ordered
        assertEquals(3, bucket.timestampsList.size());
        // test the timestamps list
        assertEquals(tsAdded1, bucket.timestampsList.getFirst());
        assertEquals(tsAdded2, bucket.timestampsList.get(1));
        assertEquals(tsAdded3, bucket.timestampsList.getLast());
    }

}