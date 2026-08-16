package com.polarisdigitech.boxdeliveryservice.db.item;

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
                query = "SELECT i FROM ItemEntity i WHERE i.boxId = :boxId AND i.status = :status ORDER BY i.name"),
        @NamedQuery(
                name = "ItemEntity.findAllByIds",
                query = "SELECT i FROM ItemEntity i WHERE i.id IN :ids"),
        @NamedQuery(
                name = "ItemEntity.findById",
                query = "SELECT i FROM ItemEntity i WHERE i.id = :id AND i.isDeleted = false"),

        @NamedQuery(
                name = "ItemEntity.existByCode",
                query = "SELECT COUNT(i) > 0 FROM ItemEntity i WHERE i.code = :code AND i.isDeleted = false"),

        @NamedQuery(
                name = "ItemEntity.deleteItemById",
                query = "UPDATE ItemEntity i SET i.isDeleted = true WHERE i.id = :id"),

        @NamedQuery(
                name = "ItemEntity.findAllAvailable",
                query = "SELECT i FROM ItemEntity i WHERE i.isDeleted = false AND i.status = :status")
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
    @Column(name = "box_id", nullable = true)
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
                         Instant createdAt,
                         UUID createdBy) {
        this.id = id;
        this.name = name;
        this.weightGrams = weightGrams;
        this.code = code;
        this.status = status;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }


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
