package com.modak.challenge.bucket;

public interface BucketsMap {


    /**
     * Answer the sliding window bucket for the given user/type
     *
     * @param type   the notification type to retrieve
     * @param userId the userId to retrieved
     * @return the bucket
     */
    WindowBucket getWindowBucket(String type, String userId);

}
