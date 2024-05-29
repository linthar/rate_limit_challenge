package com.modak;

import com.modak.challenge.bucket.BucketsMap;
import com.modak.challenge.bucket.WindowBucket;
import com.modak.challenge.exception.RateLimitExceededException;

class NotificationServiceImpl implements NotificationService {
    private Gateway gateway;

    private BucketsMap bucketsMap;


    public NotificationServiceImpl(Gateway gateway, BucketsMap bucketsMap) {
        this.gateway = gateway;
        this.bucketsMap = bucketsMap;
    }

    // TASK: IMPLEMENT this
    @Override
    public void send(String type, String userId, String message) {

        WindowBucket userTypeBucket = bucketsMap.getWindowBucket(type, userId);

        try {
            userTypeBucket.addNotification();
            // if there is no exception, the message is sent
            gateway.send(userId, message);
            // NOTE:
            // there is no need to update the bucket because we are in the same JVM
            // if bucketsMap is stored in a distributed repository (like Redis)
            // we need to update the bucket (and add exclusion of usage to avoid concurrent update)
        } catch (RateLimitExceededException e){

            // Assumption: this service will not throw any exception just avoid sending the message
            // and do something to keep track of it

            System.out.println(" ---rejected--->  message: "+message+" to user: " + userId);
            // we should add some metric about rejections

        }
    }

}
