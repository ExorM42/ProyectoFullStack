package com.clinica.mspaciente.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msPacienteOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-paciente API")
                        .version("1.0.0")
                        .description("API REST para gestionar pacientes y previsiones del sistema clinico.")
                        .contact(new Contact()
                                .name("Equipo Sistema Clinico")
                                .email("soporte@clinica.local"))
                        .license(new License()
                                .name("Uso academico")
                                .url("https://www.duoc.cl")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
