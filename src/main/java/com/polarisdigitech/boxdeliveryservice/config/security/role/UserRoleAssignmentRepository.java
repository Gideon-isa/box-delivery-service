package com.polarisdigitech.boxdeliveryservice.config.security.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRoleAssignmentRepository extends JpaRepository<UserRoleAssignmentJpaEntity, UserRoleAssignmentJpaEntity.Id> {
    @Query("SELECT a.id.roleName FROM UserRoleAssignmentJpaEntity a WHERE a.id.keycloakUserId = :userId")
    List<String> findRoleNamesByUserId(@Param("userId") UUID userId);
}
