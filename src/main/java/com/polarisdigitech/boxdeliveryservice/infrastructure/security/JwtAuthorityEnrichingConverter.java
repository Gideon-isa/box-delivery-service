package com.polarisdigitech.boxdeliveryservice.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthorityEnrichingConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final AppUserRoleLookupService roleLookupService;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        UUID keycloakUserId =
                UUID.fromString(jwt.getSubject());

        Set<String> authorities =
                roleLookupService.resolveAuthorities(keycloakUserId);

        List<SimpleGrantedAuthority> grantedAuthorities =
                authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        return new JwtAuthenticationToken(
                jwt,
                grantedAuthorities
        );
    }
}
