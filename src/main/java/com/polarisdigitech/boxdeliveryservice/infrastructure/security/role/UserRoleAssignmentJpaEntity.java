package com.polarisdigitech.boxdeliveryservice.infrastructure.security.role;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Table(name = "user_role_assignments")
public class UserRoleAssignmentJpaEntity {

    @EmbeddedId
    private Id id;

    public UserRoleAssignmentJpaEntity(UUID keycloakUserId, String roleName) {
        this.id = new Id(keycloakUserId, roleName);
    }

    public UUID getKeycloakUserId() {
        return id.keycloakUserId;
    }

    public String getRoleName() {
        return id.roleName;
    }

    @Embeddable
    public static class Id implements Serializable {

        @Column(name = "keycloak_user_id", nullable = false)
        private UUID keycloakUserId;

        @Column(name = "role_name", nullable = false, length = 64)
        private String roleName;

        protected Id() {
        }

        public Id(UUID keycloakUserId, String roleName) {
            this.keycloakUserId = keycloakUserId;
            this.roleName = roleName;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Id id)) return false;
            return Objects.equals(keycloakUserId, id.keycloakUserId) && Objects.equals(roleName, id.roleName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(keycloakUserId, roleName);
        }
    }
}
