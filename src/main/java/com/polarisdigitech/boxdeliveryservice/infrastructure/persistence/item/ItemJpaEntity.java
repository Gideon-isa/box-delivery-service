package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.item;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "ItemEntity")
@Table(name = "items")
@NamedQueries({
        @NamedQuery(
                name = "ItemEntity.findByBoxId",
                query = "SELECT i FROM ItemEntity i WHERE i.boxId = :boxId ORDER BY i.name"),
        @NamedQuery(
                name = "ItemEntity.findAllByIdIn",
                query = "SELECT i FROM ItemEntity i WHERE i.id IN :ids")
})
public class ItemJpaEntity {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "weight_grams", nullable = false)
    private double weightGrams;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ItemStatusJpa status;

    @Setter
    @Column(name = "box_id")
    private UUID boxId;

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


    public ItemJpaEntity(UUID id,
                         String name,
                         double weightGrams,
                         String code,
                         ItemStatusJpa status,
                         UUID boxId,
                         boolean isDeleted,
                         Instant createdAt,
                         UUID createdBy,
                         Instant modifiedAt,
                         UUID modifiedBy) {
        this.id = id;
        this.name = name;
        this.weightGrams = weightGrams;
        this.code = code;
        this.status = status;
        this.boxId = boxId;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }

}
