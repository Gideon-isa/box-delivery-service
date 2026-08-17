package com.polarisdigitech.boxdeliveryservice.config.security.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "permissions")
public class PermissionJpaEntity {
    @Id
    @Column(name = "code", length = 64)
    private String code;

    @Column(name = "description")
    private String description;

    protected PermissionJpaEntity() {
    }

    public PermissionJpaEntity(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
