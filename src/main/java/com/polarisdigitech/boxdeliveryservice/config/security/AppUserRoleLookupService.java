package com.polarisdigitech.boxdeliveryservice.config.security;

import com.polarisdigitech.boxdeliveryservice.config.security.role.RoleJpaEntity;
import com.polarisdigitech.boxdeliveryservice.config.security.role.RoleJpaRepository;
import com.polarisdigitech.boxdeliveryservice.config.security.role.UserRoleAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserRoleLookupService {

    private final UserRoleAssignmentRepository assignmentRepository;
    private final RoleJpaRepository roleRepository;

    @Cacheable
    public Set<String> resolveAuthorities(UUID keycloakUserId) {
        List<String> roleNames = assignmentRepository.findRoleNamesByUserId(keycloakUserId);
        if (roleNames.isEmpty()) {
            return Set.of();
        }

        List<RoleJpaEntity> roles = roleRepository.findByNameIn(roleNames);

        Set<String> authorities = roles.stream()
                .map(role -> "ROLE_" + role.getName())
                .collect(Collectors.toCollection(java.util.HashSet::new));

        roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> "PERMISSION_" + permission.getCode())
                .forEach(authorities::add);

        return authorities;
    }
}
