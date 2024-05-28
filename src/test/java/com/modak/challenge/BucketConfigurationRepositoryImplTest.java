package com.modak.challenge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BucketConfigurationRepositoryImplTest {

    @Test
    void addConfiguration() {
        BucketConfigurationRepositoryImpl repo = new BucketConfigurationRepositoryImpl();
        BucketConfiguration conf1 = new BucketConfiguration(60, 5);
        BucketConfiguration conf2 = new BucketConfiguration(600, 2);

        /// TEST
        /////////////////////////
        repo.addConfiguration("type1", conf1);
        repo.addConfiguration("type2", conf2);


        // ASSERT
        /////////////////////////

        assertEquals(conf1, repo.getConfiguration("type1"));
        assertEquals(conf2, repo.getConfiguration("type2"));
        assertNull(repo.getConfiguration("typeNotExists"));
    }

}