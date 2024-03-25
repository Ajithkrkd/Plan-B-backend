package com.ajith.projectservice.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "AJITH K",
                        email = "ajith2255iti@gmail.com"
                ),
                description = "OpenAPI documentation for work item Microservice",
                title = "OpenAPI specification - work item Microservice",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8084"
                ),

        },
        security = {
                @SecurityRequirement(
                        name = "BearerAuth"
                )
        }
)

@SecurityScheme(
        name = "BearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}