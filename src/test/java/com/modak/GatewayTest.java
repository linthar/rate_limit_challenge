package com.modak;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GatewayTest {

    @Test
    void send() {
        // just to add test coverage
        Gateway gateway = new Gateway();
        gateway.send("someUser", "some Message");
    }
}