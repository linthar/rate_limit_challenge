package com.modak.challenge.bucket;

import com.modak.challenge.exception.RateLimitExceededException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;

public class WindowBucket {

    public static final String WINDOW_BUCKET_IS_FULL_MESSAGE = "Window Bucket is full";

    // window time
    protected long timesliceSeconds;

    // max size of the Window
    protected int bucketSize;

    // is a sorted linked list of sent notifications timestamps
    // list size must be <=< bucketSize
    protected LinkedList<Long> timestampsList;

    public WindowBucket(long timesliceSeconds, int bucketSize) {

        this.bucketSize = bucketSize;
        this.timesliceSeconds = timesliceSeconds;
        this.timestampsList = new LinkedList<Long>();
    }

    /**
     * try to add a new Notification if there is room in this window bucket
     * throws RateLimitExceededException if bucket is full
     */
    public synchronized void addNotification() {
        Long timeNowSeconds = System.currentTimeMillis() / 1000;
        add(timeNowSeconds);
    }

    /**
     * INNER METHOD: Created to be this class testable
     * <p>
     * try to add a new Notification if there is room in this window bucket
     *
     * @param notificationTimeInSeconds notification TimeStamp in seconds
     * @throws RateLimitExceededException if bucket is full
     */
    protected void add(Long notificationTimeInSeconds) {
        // try to clean expired ts (ts that are outside current sliding window)
        removeExpiredNotifications(notificationTimeInSeconds);

        if (timestampsList.size() < bucketSize) {
            // there is room for another notification in the bucket
            // timestamps are stored from descending in order (oldest go last)
            timestampsList.addLast(notificationTimeInSeconds);
        } else {
            // check if there is room for a new notification
            throw new RateLimitExceededException(WINDOW_BUCKET_IS_FULL_MESSAGE);
        }
    }

    /**
     * Clean expired timestamps from bucket
     * keeps only the timestamps that apply this bucket window based on currentTimeInSeconds
     *
     *
     * @param currentTimeInSeconds the timestamp in Seconds to clean the bucket
     */
    protected void removeExpiredNotifications(Long currentTimeInSeconds) {
        // removes all timestamps older than timesliceSeconds (based on currentTimeInSeconds)

        //enhancement to avoid extra calls
        // there is no need to remove older ts if there is room for new ts in the bucket
        if (timestampsList.size() == bucketSize) {
            this.removeIfFromTimestampsList(ts -> (currentTimeInSeconds - ts) > timesliceSeconds);
        }
    }




    protected void removeIfFromTimestampsList(Predicate<? super Long> filter){
        // this is an optimization to avoid O(n) by calling:
        //   timestampsList.removeIf(ts -> (currentTimeInSeconds - ts) > timesliceSeconds);
        // list is sorted by ts (ascending)
        // so we can stop at the first element that does not match

            Iterator<Long> each = timestampsList.iterator();
            boolean mustRemove = true;
            while(each.hasNext() && mustRemove) {
                mustRemove = filter.test(each.next());
                if (mustRemove) {
                    each.remove();
                }
            }

        }


}
