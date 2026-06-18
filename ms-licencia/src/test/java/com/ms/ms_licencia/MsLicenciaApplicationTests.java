package com.ms.ms_licencia;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class MsLicenciaApplicationTests {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> new MsLicenciaApplication());
    }
}