package com.polarisdigitech.boxdeliveryservice.infrastructure.security.role;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
public class RoleJpaEntity {

    @Id
    @Column(name = "name", length = 64)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_name"),
            inverseJoinColumns = @JoinColumn(name = "permission_code")
    )
    private Set<PermissionJpaEntity> permissions = new HashSet<>();


    public RoleJpaEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<PermissionJpaEntity> getPermissions() {
        return permissions;
    }
}
