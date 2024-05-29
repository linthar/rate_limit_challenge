package com.modak.challenge.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitConfigurationRepositoryImplTest {

    @Test
    void addConfiguration() {
        RateLimitConfigurationRepositoryImpl repo = new RateLimitConfigurationRepositoryImpl();
        /// TEST
        /////////////////////////
        repo.addConfiguration("type1", 60, 5);
        repo.addConfiguration("type2", 123, 456);

        // ASSERT
        /////////////////////////

        assertEquals(60, repo.getConfiguration("type1").timeInSeconds());
        assertEquals(5, repo.getConfiguration("type1").notificationsLimitCount());

        assertEquals(123, repo.getConfiguration("type2").timeInSeconds());
        assertEquals(456, repo.getConfiguration("type2").notificationsLimitCount());


        // check default configuration is returned
        assertEquals(RateLimitConfigurationRepositoryImpl.DEFAULT_CONFIGURATION, repo.getConfiguration("typeNotExists"));

    }

}