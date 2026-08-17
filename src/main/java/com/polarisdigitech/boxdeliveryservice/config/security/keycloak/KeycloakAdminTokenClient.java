package com.polarisdigitech.boxdeliveryservice.config.security.keycloak;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KeycloakAdminTokenClient {
    private final KeycloakAdminProperties properties;
    private final RestClient restClient = RestClient.create();

    public String fetchAdminAccessToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", properties.clientId());
        form.add("client_secret", properties.clientSecret());

        Map<String, Object> response = restClient.post()
                .uri(properties.tokenUrl())
                .contentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);

        if (response == null || response.get("access_token") == null) {
            throw new KeycloakProvisioningException("Failed to obtain Keycloak admin access token");
        }
        return (String) response.get("access_token");
    }
}
