package com.polarisdigitech.boxdeliveryservice.delivery.domain;

import com.polarisdigitech.boxdeliveryservice.box.domain.BoxConstants;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemId;
import com.polarisdigitech.boxdeliveryservice.shared.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class Delivery extends AggregateRoot<UUID> {
    private final double locationDistance;
    private final double boxSetSpeed;
    private final BoxId boxId;
    private final List<ItemId> ItemIds;
    private final Instant startTime;
    private Instant arrivalTime;
    private Instant returnedTime;
    private boolean isDelivered;
    private boolean isReturned;

    private Delivery(UUID id,
                    UUID createdBy,
                    double locationDistance,
                    double boxSetSpeed,
                    BoxId boxId,
                    List<ItemId> itemsIds,
                    Instant startTime,
                    Instant arrivalTime,
                    Instant returnedTime,
                    boolean isDelivered,
                    boolean isReturned) {
        super(id, createdBy);
        this.locationDistance = locationDistance;
        this.boxSetSpeed = boxSetSpeed;
        this.boxId = boxId;
        this.ItemIds = itemsIds;
        this.startTime = startTime;
        this.arrivalTime = arrivalTime;
        this.returnedTime = returnedTime;
    }

    public static Result<Delivery, DomainError> create(
            UUID createdBy,
            double locationDistance,
            double boxSetSpeed,
            BoxId boxId,
            List<ItemId> itemIds) {

        if (boxId == null) {
            return Result.failure(ValidationError.of("boxId", "boxId can not be empty or null"));
        }
       if (createdBy == null) {
           return Result.failure(ValidationError.of("createdBy", "createBy can not be empty"));
       }
       if (locationDistance <= 0) {
           return Result.failure(ValidationError.of("locationDistance", "location distance can not be zero"));
       }

        if (boxSetSpeed <= 0 || boxSetSpeed > BoxConstants.MAX_SPEED) {
            return Result.failure(ValidationError.of("boxSetSpeed", "Box speed can not be set to zero or greater than " + BoxConstants.MAX_SPEED));
        }
        return Result.success(new Delivery(
                UUID.randomUUID(),
                createdBy,
                locationDistance,
                boxSetSpeed,
                boxId,
                itemIds,
                Instant.now(),
                null,
                null,
                false,
                false));

    }

    public static Result<Delivery, DomainError> reconstitute(
            UUID id, UUID createdBy, double locationDistance, double boxSetSpeed, BoxId boxId,
            List<ItemId> itemIds, Instant startTime, Instant arrivalTime, Instant returnedTime,
            boolean isDelivered, boolean isReturned) {
        if (id == null || boxId == null) {
            return Result.failure(ValidationError.of("delivery", "Delivery reconstitution requires id and boxId"));
        }
        return Result.success(new Delivery(UUID.randomUUID(), createdBy, locationDistance, boxSetSpeed, boxId,
                itemIds, startTime, arrivalTime, returnedTime, isDelivered, isReturned));
    }

    public Result<Delivery, DomainError> markAsReturned() {
        if (!isDelivered) {
            return Result.failure(BusinessRuleViolation.of("NOT_YET_DELIVERED", "Cannot return a delivery that has not been delivered"));
        }
        if (isReturned) {
            return Result.failure(BusinessRuleViolation.of("ALREADY_RETURNED", "Delivery " + getId() + " is already marked as returned"));
        }
        this.returnedTime = Instant.now();
        this.isReturned = true;
        return Result.success(this);
    }

    public Result<Delivery, DomainError> markAsDelivered() {
        if (isDelivered) {
            return Result.failure(BusinessRuleViolation.of("ALREADY_DELIVERED", "Delivery " + getId() + " is already marked as delivered"));
        }
        if (isReturned) {
            return Result.failure(BusinessRuleViolation.of("ALREADY_RETURNED", "Cannot deliver a delivery that has already been returned"));
        }
        this.arrivalTime = Instant.now();
        this.isDelivered = true;
        return Result.success(this);
    }

    public double getLocationDistance() { return locationDistance; }
    public double getBoxSetSpeed() { return boxSetSpeed; }
    public BoxId getBoxId() { return boxId; }
    public List<ItemId> getItemIds() { return ItemIds; }
    public Instant getStartTime() { return startTime; }
    public Instant getArrivalTime() { return arrivalTime; }
    public Instant getReturnedTime() { return returnedTime; }
    public boolean isDelivered() { return isDelivered; }
    public boolean isReturned() { return isReturned; }

}
