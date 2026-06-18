package com.ms.ms_auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class MsAuthApplicationTests {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> new MsAuthApplication());
    }
}