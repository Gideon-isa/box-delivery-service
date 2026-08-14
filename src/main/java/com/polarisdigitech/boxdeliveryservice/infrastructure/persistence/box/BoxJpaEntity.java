package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.box;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA persistence model mirroring the pure Box aggregate root.
 * Box holds no relationship to items
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "Box")
@Table(name = "boxes")
@NamedQueries({
        @NamedQuery(
                name = "Box.findByTxRef",
                query = "SELECT b FROM Box b WHERE b.txRef = :txRef"),
        @NamedQuery(
                name = "Box.existsByTxRef",
                query = "SELECT COUNT(b) > 0 FROM Box b WHERE b.txRef = :txRef"),
        @NamedQuery(
                name = "Box.findAvailableForLoading",
                query = "SELECT b FROM Box b WHERE b.state = IDLE "
                        + "AND b.batteryLevel >= 25")
})
public class BoxJpaEntity {
    @Id
    private UUID id;

    @Column(name = "tx_ref", nullable = false, unique = true, length = 20)
    private String txRef;

    @Getter
    @Column(name = "weight_limit", nullable = false)
    private double weightLimit;

    @Column(name = "battery_level", nullable = false)
    private double batteryLevel;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 20)
    private BoxStateJpa state;

    @Setter
    @Column(name = "total_items_weight", nullable = false)
    private double totalItemsWeight;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private UUID createdBy;

    @Setter
    @Column(name = "modified_at")
    private Instant modifiedAt;

    @Setter
    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Version
    private long version;

    public BoxJpaEntity(UUID id, String txRef, double weightLimitGrams, double batteryLevel,
                        BoxStateJpa state, double totalItemsWeight, boolean isDeleted,
                        Instant createdAt, UUID createdBy, Instant modifiedAt, UUID modifiedBy) {
        this.id = id;
        this.txRef = txRef;
        this.weightLimit = weightLimitGrams;
        this.batteryLevel = batteryLevel;
        this.state = state;
        this.totalItemsWeight = totalItemsWeight;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }

}
