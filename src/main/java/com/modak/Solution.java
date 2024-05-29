package com.modak;


import com.modak.challenge.bucket.BucketsMap;
import com.modak.challenge.bucket.BucketsMapImpl;
import com.modak.challenge.configuration.RateLimitConfigurationRepository;
import com.modak.challenge.configuration.RateLimitConfigurationRepositoryImpl;

class Solution {
    public static void main(String[] args) {

        BucketsMap bucketsMap = new BucketsMapImpl(getRateLimitConfigurationRepository());
        NotificationServiceImpl service = new NotificationServiceImpl(new Gateway(), bucketsMap);

        service.send("marketing", "user", "marketing 1");
        service.send("marketing", "user", "marketing 2");
        service.send("marketing", "user", "marketing 3");
        service.send("marketing", "another user", "marketing 1");
        service.send("update", "user", "update 1");


        // this one will be rejected (WindowBucket will fail throwing a RateLimitExceededException)
        // and NotificationServiceImpl will not sent the message to Gateway
        service.send("marketing", "user", "marketing 4");

    }

    public static RateLimitConfigurationRepository getRateLimitConfigurationRepository() {

        RateLimitConfigurationRepository configurationRepository = new RateLimitConfigurationRepositoryImpl();
        // copying example configuration

       // - Status: not more than 2 per minute for each recipient
        long minuteInSecs = 60;
        configurationRepository.addConfiguration("status", minuteInSecs, 2);

        // - News: not more than 1 per day for each recipient
        long dayInSecs = 86400;//  = 24*60*60 secs;
        configurationRepository.addConfiguration("news", dayInSecs, 1);

        // - Marketing: not more than 3 per hour for each recipient
        long hourInSecs = 3600; // 60*60
        configurationRepository.addConfiguration("marketing", hourInSecs, 3);

        return configurationRepository;

    }

}
