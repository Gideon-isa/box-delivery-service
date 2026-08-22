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


    private final KeycloakUserProvisioningService keycloakUserProvisioningService;
    private final UserRoleAssignmentRepository userRoleAssignmentRepository;

    @Transactional
    public SignupResponse signup(SignupRequest request, Role role) {
        var keycloakUserId = keycloakUserProvisioningService.createUser(
                request.username(), request.email(), request.password());

        userRoleAssignmentRepository.save(new UserRoleAssignmentJpaEntity(keycloakUserId, role.name()));
        return new SignupResponse(keycloakUserId, request.username(), request.email(), role.name());
    }
}
