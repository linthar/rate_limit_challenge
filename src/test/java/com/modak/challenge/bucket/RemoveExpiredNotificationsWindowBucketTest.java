package com.modak.challenge.bucket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * This tests were splitted by method to make it more readable
 */
class RemoveExpiredNotificationsWindowBucketTest {


    @Test
    void removeExpiredNotifications() {

        // slice window : max 3 notifications / 1 minute (=60 secs)
        WindowBucket windowBucket = new WindowBucket(60, 3);
        Long nowInSeconds = System.currentTimeMillis() / 1000;

        // complete the bucket
        Long nowInSeconds_61before = nowInSeconds - 61;
        Long nowInSeconds_60before = nowInSeconds - 60;
        Long nowInSeconds_30before = nowInSeconds - 30;

        // timestamps are stored from descending in order (oldest go fist)
        windowBucket.timestampsList.addLast(nowInSeconds_61before);
        windowBucket.timestampsList.addLast(nowInSeconds_60before);
        windowBucket.timestampsList.addLast(nowInSeconds_30before);

        assertEquals(3, windowBucket.timestampsList.size());
        // test the timestampsList
        assertEquals(nowInSeconds_61before, windowBucket.timestampsList.getFirst());
        assertEquals(nowInSeconds_60before, windowBucket.timestampsList.get(1));
        assertEquals(nowInSeconds_30before, windowBucket.timestampsList.getLast());


        /// TEST
        /////////////////////////
        windowBucket.removeExpiredNotifications(nowInSeconds);

        // ASSERT
        /////////////////////////

        assertEquals(2, windowBucket.timestampsList.size());

        // test the timestamps list
        assertEquals(nowInSeconds_60before, windowBucket.timestampsList.getFirst());
        assertEquals(nowInSeconds_30before, windowBucket.timestampsList.getLast());
    }

    @Test
    void removeExpiredNotifications_WhenThereIsNothingToRemove() {

        // slice window : max 5 notifications / 30 minutes (=1800 secs)
        WindowBucket windowBucket = new WindowBucket(1800, 5);

        Long nowInSeconds = System.currentTimeMillis() / 1000;

        // complete the bucket
        Long ts4 = nowInSeconds - 61;
        Long ts3 = nowInSeconds - 60;
        Long ts2 = nowInSeconds - 30;
        Long ts1 = nowInSeconds - 10;
        Long ts0 = nowInSeconds - 5;

        // timestamps are stored from descending in order (oldest go fist)
        windowBucket.timestampsList.addLast(ts4);
        windowBucket.timestampsList.addLast(ts3);
        windowBucket.timestampsList.addLast(ts2);
        windowBucket.timestampsList.addLast(ts1);
        windowBucket.timestampsList.addLast(ts0);

        // test the timestamps List
        assertEquals(5, windowBucket.timestampsList.size());
        assertEquals(ts4, windowBucket.timestampsList.get(0));
        assertEquals(ts3, windowBucket.timestampsList.get(1));
        assertEquals(ts2, windowBucket.timestampsList.get(2));
        assertEquals(ts1, windowBucket.timestampsList.get(3));
        assertEquals(ts0, windowBucket.timestampsList.get(4));

        /// TEST
        /////////////////////////
        windowBucket.removeExpiredNotifications(nowInSeconds);

        // ASSERT
        /////////////////////////
        // no ts must be removed (because all are < 30 mins older)

        // test the timestamps List
        assertEquals(5, windowBucket.timestampsList.size());
        assertEquals(ts4, windowBucket.timestampsList.get(0));
        assertEquals(ts3, windowBucket.timestampsList.get(1));
        assertEquals(ts2, windowBucket.timestampsList.get(2));
        assertEquals(ts1, windowBucket.timestampsList.get(3));
        assertEquals(ts0, windowBucket.timestampsList.get(4));
    }




    @Test
    void removeExpiredNotifications_WhenThereIsRoomForMore() {

        // slice window : max 3 notifications / 30 secs
        WindowBucket windowBucket = new WindowBucket(30, 3);
        Long nowInSeconds = System.currentTimeMillis() / 1000;

        // complete the bucket
        Long nowInSeconds_61before = nowInSeconds - 61;
        Long nowInSeconds_60before = nowInSeconds - 60;

        // timestamps are stored from descending in order (oldest go fist)
        windowBucket.timestampsList.addLast(nowInSeconds_61before);
        windowBucket.timestampsList.addLast(nowInSeconds_60before);


        assertEquals(2, windowBucket.timestampsList.size());
        // test the timestampsList
        assertEquals(nowInSeconds_61before, windowBucket.timestampsList.getFirst());
        assertEquals(nowInSeconds_60before, windowBucket.timestampsList.getLast());


        /// TEST
        /////////////////////////
        windowBucket.removeExpiredNotifications(nowInSeconds);

        // ASSERT
        /////////////////////////
        // both ts are outside os the window (window has 30 secs, ts are 1 minute older)
        // but removeExpiredNotifications does nothing because there is still room to add more.
        assertEquals(2, windowBucket.timestampsList.size());

        // test the timestamps list
        assertEquals(nowInSeconds_61before, windowBucket.timestampsList.getFirst());
        assertEquals(nowInSeconds_60before, windowBucket.timestampsList.getLast());
    }



    @Test
    void removeExpiredNotifications_WhenMustRemoveAll() {

        // slice window : max 3 notifications / 30 secs
        WindowBucket windowBucket = new WindowBucket(30, 2);
        Long nowInSeconds = System.currentTimeMillis() / 1000;

        // complete the bucket
        Long nowInSeconds_61before = nowInSeconds - 61;
        Long nowInSeconds_60before = nowInSeconds - 60;

        // timestamps are stored from descending in order (oldest go fist)
        windowBucket.timestampsList.addLast(nowInSeconds_61before);
        windowBucket.timestampsList.addLast(nowInSeconds_60before);


        assertEquals(2, windowBucket.timestampsList.size());
        // test the timestampsList
        assertEquals(nowInSeconds_61before, windowBucket.timestampsList.getFirst());
        assertEquals(nowInSeconds_60before, windowBucket.timestampsList.getLast());


        /// TEST
        /////////////////////////
        windowBucket.removeExpiredNotifications(nowInSeconds);

        // ASSERT
        /////////////////////////
        // both ts are outside os the window (window has 30 secs, ts are 1 minute older)
        // removeExpiredNotifications has clean the list because there was no room to add more.
        assertEquals(0, windowBucket.timestampsList.size());

    }



}