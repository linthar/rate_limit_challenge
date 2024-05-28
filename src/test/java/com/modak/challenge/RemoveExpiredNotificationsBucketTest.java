package com.modak.challenge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * This tests were splitted by method to make it more readable
 */
class RemoveExpiredNotificationsBucketTest {


    @Test
    void removeExpiredNotifications() {

        // slice window : max 3 notifications / 1 minute (=60 secs)
        Bucket bucket = new Bucket(60, 3);
        Long nowInSeconds = System.currentTimeMillis() / 1000;

        // complete the bucket
        Long nowInSeconds_61before = nowInSeconds - 61;
        Long nowInSeconds_60before = nowInSeconds - 60;
        Long nowInSeconds_30before = nowInSeconds - 30;

        // timestamps are stored from descending in order (oldest go fist)
        bucket.timestampsList.addLast(nowInSeconds_61before);
        bucket.timestampsList.addLast(nowInSeconds_60before);
        bucket.timestampsList.addLast(nowInSeconds_30before);

        assertEquals(3, bucket.timestampsList.size());
        // test the timestampsList
        assertEquals(nowInSeconds_61before, bucket.timestampsList.getFirst());
        assertEquals(nowInSeconds_60before, bucket.timestampsList.get(1));
        assertEquals(nowInSeconds_30before, bucket.timestampsList.getLast());


        /// TEST
        /////////////////////////
        bucket.removeExpiredNotifications(nowInSeconds);

        // ASSERT
        /////////////////////////

        assertEquals(2, bucket.timestampsList.size());

        // test the timestamps list
        assertEquals(nowInSeconds_60before, bucket.timestampsList.getFirst());
        assertEquals(nowInSeconds_30before, bucket.timestampsList.getLast());
    }

    @Test
    void removeExpiredNotifications_WhenThereIsNothingToRemove() {

        // slice window : max 5 notifications / 30 minutes (=1800 secs)
        Bucket bucket = new Bucket(1800, 5);

        Long nowInSeconds = System.currentTimeMillis() / 1000;

        // complete the bucket
        Long ts4 = nowInSeconds - 61;
        Long ts3 = nowInSeconds - 60;
        Long ts2 = nowInSeconds - 30;
        Long ts1 = nowInSeconds - 10;
        Long ts0 = nowInSeconds - 5;

        // timestamps are stored from descending in order (oldest go fist)
        bucket.timestampsList.addLast(ts4);
        bucket.timestampsList.addLast(ts3);
        bucket.timestampsList.addLast(ts2);
        bucket.timestampsList.addLast(ts1);
        bucket.timestampsList.addLast(ts0);

        // test the timestamps List
        assertEquals(5, bucket.timestampsList.size());
        assertEquals(ts4, bucket.timestampsList.get(0));
        assertEquals(ts3, bucket.timestampsList.get(1));
        assertEquals(ts2, bucket.timestampsList.get(2));
        assertEquals(ts1, bucket.timestampsList.get(3));
        assertEquals(ts0, bucket.timestampsList.get(4));

        /// TEST
        /////////////////////////
        bucket.removeExpiredNotifications(nowInSeconds);

        // ASSERT
        /////////////////////////
        // no ts must be removed (because all are < 30 mins older)

        // test the timestamps List
        assertEquals(5, bucket.timestampsList.size());
        assertEquals(ts4, bucket.timestampsList.get(0));
        assertEquals(ts3, bucket.timestampsList.get(1));
        assertEquals(ts2, bucket.timestampsList.get(2));
        assertEquals(ts1, bucket.timestampsList.get(3));
        assertEquals(ts0, bucket.timestampsList.get(4));
    }




    @Test
    void removeExpiredNotifications_WhenThereIsRoomForMore() {

        // slice window : max 3 notifications / 30 secs
        Bucket bucket = new Bucket(30, 3);
        Long nowInSeconds = System.currentTimeMillis() / 1000;

        // complete the bucket
        Long nowInSeconds_61before = nowInSeconds - 61;
        Long nowInSeconds_60before = nowInSeconds - 60;

        // timestamps are stored from descending in order (oldest go fist)
        bucket.timestampsList.addLast(nowInSeconds_61before);
        bucket.timestampsList.addLast(nowInSeconds_60before);


        assertEquals(2, bucket.timestampsList.size());
        // test the timestampsList
        assertEquals(nowInSeconds_61before, bucket.timestampsList.getFirst());
        assertEquals(nowInSeconds_60before, bucket.timestampsList.getLast());


        /// TEST
        /////////////////////////
        bucket.removeExpiredNotifications(nowInSeconds);

        // ASSERT
        /////////////////////////
        // both ts are outside os the window (window has 30 secs, ts are 1 minute older)
        // but removeExpiredNotifications does nothing because there is still room to add more.
        assertEquals(2, bucket.timestampsList.size());

        // test the timestamps list
        assertEquals(nowInSeconds_61before, bucket.timestampsList.getFirst());
        assertEquals(nowInSeconds_60before, bucket.timestampsList.getLast());
    }



    @Test
    void removeExpiredNotifications_WhenMustRemoveAll() {

        // slice window : max 3 notifications / 30 secs
        Bucket bucket = new Bucket(30, 2);
        Long nowInSeconds = System.currentTimeMillis() / 1000;

        // complete the bucket
        Long nowInSeconds_61before = nowInSeconds - 61;
        Long nowInSeconds_60before = nowInSeconds - 60;

        // timestamps are stored from descending in order (oldest go fist)
        bucket.timestampsList.addLast(nowInSeconds_61before);
        bucket.timestampsList.addLast(nowInSeconds_60before);


        assertEquals(2, bucket.timestampsList.size());
        // test the timestampsList
        assertEquals(nowInSeconds_61before, bucket.timestampsList.getFirst());
        assertEquals(nowInSeconds_60before, bucket.timestampsList.getLast());


        /// TEST
        /////////////////////////
        bucket.removeExpiredNotifications(nowInSeconds);

        // ASSERT
        /////////////////////////
        // both ts are outside os the window (window has 30 secs, ts are 1 minute older)
        // removeExpiredNotifications has clean the list because there was no room to add more.
        assertEquals(0, bucket.timestampsList.size());

    }



}