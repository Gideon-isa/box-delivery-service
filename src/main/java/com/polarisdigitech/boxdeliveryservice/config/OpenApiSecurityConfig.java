package com.polarisdigitech.boxdeliveryservice.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Polaris digitech - Box delivery service", version = "1.0"),
        security = @SecurityRequirement(name = "keycloak")
)
@SecurityScheme(
        name = "keycloak",
        type = SecuritySchemeType.OAUTH2,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER,
        flows = @OAuthFlows(
                password = @OAuthFlow(
                        authorizationUrl = "http://localhost:8081/realms/boxdelivery/protocol/openid-connect/auth",
                        tokenUrl = "http://localhost:8081/realms/boxdelivery/protocol/openid-connect/token"
                )
        )
)
public class OpenApiSecurityConfig {


    @Bean
    public GlobalOperationCustomizer globalApiResponses() {
        return (operation, handlerMethod) -> {

            ApiResponses responses = operation.getResponses();

            responses.addApiResponse(
                    "400",
                    new ApiResponse()
                            .description("Bad request")
            );

            responses.addApiResponse(
                    "401",
                    new ApiResponse()
                            .description("Authentication required")
            );

            responses.addApiResponse(
                    "403",
                    new ApiResponse()
                            .description("Access denied")
            );

            responses.addApiResponse(
                    "404",
                    new ApiResponse()
                            .description("Resource not found")
            );

            responses.addApiResponse(
                    "500",
                    new ApiResponse()
                            .description("Internal server error")
            );

            return operation;
        };
    }
}
