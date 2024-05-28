package com.modak;

import com.modak.challenge.Bucket;

import java.util.HashMap;
import java.util.Map;

class NotificationServiceImpl implements NotificationService {
    private Gateway gateway;

    private Map<String, Bucket> bucketsMap = new HashMap<>();


    public NotificationServiceImpl(Gateway gateway) {
        this.gateway = gateway;
    }

    // TASK: IMPLEMENT this
    @Override
    public void send(String type, String userId, String message) {
        throw new RuntimeException("not implemented - fix this");
    }






}
