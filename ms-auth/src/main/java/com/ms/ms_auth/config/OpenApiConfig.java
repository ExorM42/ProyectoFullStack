package com.ms.ms_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msAuthOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-auth API")
                        .version("1.0.0")
                        .description("API REST para registrar usuarios, iniciar sesion y emitir tokens JWT del sistema clinico.")
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