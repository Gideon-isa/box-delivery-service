package com.polarisdigitech.boxdeliveryservice.config.security;

import com.polarisdigitech.boxdeliveryservice.application.security.CurrentUser;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class SecurityCurrentUser implements CurrentUser {
    @Override
    public UUID getId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new IllegalStateException("No authenticated user");
        }

        return UUID.fromString(Objects.requireNonNull(jwtAuth.getToken().getSubject()));
    }

    @Override
    public String getUsername() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new IllegalStateException("No authenticated user");
        }
        return jwtAuth.getToken().getClaimAsString("preferred_username");
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.isAuthenticated();
    }
}

