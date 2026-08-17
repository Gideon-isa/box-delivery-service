package com.polarisdigitech.boxdeliveryservice.auth;

import com.polarisdigitech.boxdeliveryservice.config.security.keycloak.KeycloakUserProvisioningService;
import com.polarisdigitech.boxdeliveryservice.config.security.role.UserRoleAssignmentJpaEntity;
import com.polarisdigitech.boxdeliveryservice.config.security.role.UserRoleAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {

    private static final String DEFAULT_ROLE = "VIEWER";

    private final KeycloakUserProvisioningService keycloakUserProvisioningService;
    private final UserRoleAssignmentRepository userRoleAssignmentRepository;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        var keycloakUserId = keycloakUserProvisioningService.createUser(
                request.username(), request.email(), request.password());

        userRoleAssignmentRepository.save(new UserRoleAssignmentJpaEntity(keycloakUserId, DEFAULT_ROLE));
        return new SignupResponse(keycloakUserId, request.username(), request.email(), DEFAULT_ROLE);
    }
}
