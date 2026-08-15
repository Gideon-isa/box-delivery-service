package com.polarisdigitech.boxdeliveryservice.shared;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public abstract class AuditableEntity<ID> extends Entity<ID> {

    private final Instant createdAt;
    private Instant modifiedAt;
    private final UUID createdBy;
    private UUID modifiedBy;

    protected AuditableEntity(ID id, UUID createdBy) {
        super(id);
        this.createdBy = createdBy;
        this.createdAt = Instant.now();
        this.modifiedAt = null;
        this.modifiedBy = null;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void markModified(UUID userId) {
        this.modifiedBy = Objects.requireNonNull(userId);
        this.modifiedAt = Instant.now();
    }

    public UUID getModifiedBy() {
        return modifiedBy;
    }
}
