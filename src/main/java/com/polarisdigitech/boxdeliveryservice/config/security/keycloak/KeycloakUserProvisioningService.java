package com.polarisdigitech.boxdeliveryservice.config.security.keycloak;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KeycloakUserProvisioningService {

    private final KeycloakAdminProperties properties;
    private final KeycloakAdminTokenClient tokenClient;
    private final RestClient restClient = RestClient.create();


    public UUID createUser(String username, String email, String password) {
        String adminToken = tokenClient.fetchAdminAccessToken();

        Map<String, Object> body = Map.of(
                "username", username,
                "email", email,
                "enabled", true,
                "emailVerified", false,
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", password,
                        "temporary", false
                ))
        );

        ResponseEntity<Void> response;
        try {
            response = restClient.post()
                    .uri(properties.usersUrl())
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (org.springframework.web.client.HttpClientErrorException.Conflict ex) {
            throw new KeycloakProvisioningException("A user with that username or email already exists");
        } catch (Exception ex) {
            throw new KeycloakProvisioningException("Failed to create user in Keycloak", ex);
        }

        URI location = response.getHeaders().getLocation();
        if (location == null) {
            throw new KeycloakProvisioningException("Keycloak did not return a Location header for the created user");
        }

        String path = location.getPath();
        String userId = path.substring(path.lastIndexOf('/') + 1);
        return UUID.fromString(userId);
    }
}
