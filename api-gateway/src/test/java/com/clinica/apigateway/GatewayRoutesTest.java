package com.clinica.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GatewayRoutesTest {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void cargaRutasPrincipalesDeMicroservicios() {
        Set<String> routeIds = routeLocator.getRoutes()
                .map(route -> route.getId())
                .collect(Collectors.toSet())
                .block();

        assertThat(routeIds)
                .contains("ms-paciente", "ms-doctor", "ms-receta", "ms-laboratorio", "ms-historial-clinico");
    }
}

