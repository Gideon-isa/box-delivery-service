package com.polarisdigitech.boxdeliveryservice.infrastructure.persistence.delivery;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "Delivery")
@Table(name = "deliveries")
@NamedQueries({
        @NamedQuery(
                name = "Delivery.findByBoxId",
                query = "SELECT d FROM Delivery d WHERE d.boxId = :boxId AND d.isDeleted = false"),
        @NamedQuery(
                name = "Delivery.findActive",
                query = "SELECT d FROM Delivery d WHERE d.isDelivered = false AND d.isReturned = false AND d.isDeleted = false")
})
public class DeliveryJpaEntity {

    @Id
    private UUID id;

    @Column(name = "location_distance", nullable = false)
    private double locationDistance;

    @Column(name = "box_set_speed", nullable = false)
    private double boxSetSpeed;

    @Column(name = "box_id", nullable = false)
    private UUID boxId;

    @ElementCollection
    @CollectionTable(name = "delivery_items", joinColumns = @JoinColumn(name = "delivery_id"))
    @Column(name = "item_id", nullable = false)
    private List<UUID> itemIds;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Setter
    @Column(name = "arrival_time")
    private Instant arrivalTime;

    @Setter
    @Column(name = "returned_time")
    private Instant returnedTime;

    @Setter
    @Column(name = "is_delivered", nullable = false)
    private boolean isDelivered;

    @Setter
    @Column(name = "is_returned", nullable = false)
    private boolean isReturned;

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

    public DeliveryJpaEntity(UUID id, double locationDistance, double boxSetSpeed, UUID boxId,
                             List<UUID> itemIds, Instant startTime, Instant arrivalTime, Instant returnedTime,
                             boolean isDelivered, boolean isReturned, boolean isDeleted,
                             Instant createdAt, UUID createdBy, Instant modifiedAt, UUID modifiedBy) {
        this.id = id;
        this.locationDistance = locationDistance;
        this.boxSetSpeed = boxSetSpeed;
        this.boxId = boxId;
        this.itemIds = itemIds;
        this.startTime = startTime;
        this.arrivalTime = arrivalTime;
        this.returnedTime = returnedTime;
        this.isDelivered = isDelivered;
        this.isReturned = isReturned;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }
}
