package com.polarisdigitech.boxdeliveryservice.delivery.domain;

import com.polarisdigitech.boxdeliveryservice.box.domain.BoxConstants;
import com.polarisdigitech.boxdeliveryservice.box.domain.BoxId;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemId;
import com.polarisdigitech.boxdeliveryservice.shared.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class Delivery extends AggregateRoot<UUID> {
    private final String destinationName;
    private final double dispatchedLocationLatitude;
    private final double dispatchedLocationLongitude;
    private final double destinationLatitude;
    private final double destinationLongitude;
    private final double destinationDistance; //km
    private final double boxSetSpeed;
    private final BoxId boxId;
    private final List<ItemId> ItemIds;
    private final Instant startTime;
    private final Instant estimatedArrivalTime;
    private Instant arrivalTime;
    private boolean isDelivered;
    private boolean isReturned;

    private Delivery(
                     UUID id,
                     String remoteLocationName,
                     double dispatchedLatitude,
                     double dispatchedLongitude,
                     double destinationLatitude,
                     double destinationLongitude,
                     double locationDistance,
                     double boxSetSpeed,
                     BoxId boxId,
                     List<ItemId> itemsIds,
                     Instant startTime,
                     Instant estimatedArrivalTime,
                     Instant arrivalTime,
                     boolean isDelivered,
                     boolean isReturned,
                     UUID createdBy
                     ) {
        super(id, createdBy);
        this.destinationDistance = locationDistance;
        this.destinationName = remoteLocationName;
        this.dispatchedLocationLatitude = dispatchedLatitude;
        this.dispatchedLocationLongitude = dispatchedLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.boxSetSpeed = boxSetSpeed;
        this.boxId = boxId;
        this.ItemIds = itemsIds;
        this.startTime = startTime;
        this.arrivalTime = arrivalTime;
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public static Result<Delivery, DomainError> create(
            String remoteLocationName,
            double dispatchedLatitude,
            double dispatchedLongitude,
            double destinationLatitude,
            double destinationLongitude,
            double locationDistance,
            Instant estimatedArrivalTime,
            double boxSetSpeed,
            BoxId boxId,
            List<ItemId> itemIds,
            UUID createdBy) {

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
                remoteLocationName,
                dispatchedLatitude,
                dispatchedLongitude,
                destinationLatitude,
                destinationLongitude,
                locationDistance,
                boxSetSpeed,
                boxId,
                itemIds,
                Instant.now(),
                estimatedArrivalTime,
                null,
                false,
                false,
                createdBy));
    }

    public static Result<Delivery, DomainError> reconstitute(
            UUID id,
            String remoteLocationName,
            double dispatchedLatitude,
            double dispatchedLongitude,
            double destinationLatitude,
            double destinationLongitude,
            double locationDistance,
            double boxSetSpeed,
            BoxId boxId,
            List<ItemId> itemIds,
            Instant startTime,
            Instant arrivalTime,
            Instant estimatedArrivalTime,
            boolean isDelivered,
            boolean isReturned,
            UUID createdBy) {

        if (id == null || boxId == null) {
            return Result.failure(ValidationError.of("delivery", "Delivery reconstitution requires id and boxId"));
        }
        return Result.success(new Delivery(
                id,
                remoteLocationName,
                dispatchedLatitude,
                dispatchedLongitude,
                destinationLatitude,
                destinationLongitude,
                locationDistance,
                boxSetSpeed,
                boxId,
                itemIds,
                startTime,
                estimatedArrivalTime,
                arrivalTime,
                isDelivered,
                isReturned,
                createdBy));
    }

    public Result<Delivery, DomainError> markAsReturned() {
        if (!isDelivered) {
            return Result.failure(BusinessRuleViolation.of("NOT_YET_DELIVERED", "Cannot return a delivery that has not been delivered"));
        }
        if (isReturned) {
            return Result.failure(BusinessRuleViolation.of("ALREADY_RETURNED", "Delivery " + getId() + " is already marked as returned"));
        }

        this.isReturned = true;
        return Result.success(this);
    }

    public Result<Delivery, DomainError> markAsDelivered() {
        if (isDelivered) {
            return Result.failure(BusinessRuleViolation.of(
                    "ALREADY_DELIVERED", "Delivery " + getId() + " is already marked as delivered"));
        }
        if (isReturned) {
            return Result.failure(BusinessRuleViolation.of(
                    "ALREADY_RETURNED",
                    "Cannot deliver a delivery that has already been returned"));
        }
        this.arrivalTime = Instant.now();
        this.isDelivered = true;

        return Result.success(this);
    }

    public static  Result<Double, DomainError> calculateDistance(
                                                            double currentLatitude,
                                                           double currentLongitude,
                                                           double destinationLatitude,
                                                           double destinationLongitude) {

        var currentLocationResult = Coordinates.of(currentLatitude, currentLongitude);
        var targetLocationResult = Coordinates.of(destinationLatitude, destinationLongitude);

        if (currentLocationResult.isFailure()) {
            return Result.failure(currentLocationResult.getError());
        }

        if (targetLocationResult.isFailure()) {
            return Result.failure(targetLocationResult.getError());
        }

        double distance = currentLocationResult.getValue().distanceToKm(targetLocationResult.getValue());
        return Result.success(distance);
    }

    public String getDestinationName() {return destinationName;}
    public double getDispatchedLocationLatitude() {return dispatchedLocationLatitude;}
    public double getDispatchedLocationLongitude() {return dispatchedLocationLongitude;}
    public double getDestinationLatitude() { return destinationLatitude;}
    public double getDestinationLongitude() {return destinationLongitude;}
    public double getDestinationDistance() { return destinationDistance; }
    public double getBoxSetSpeed() { return boxSetSpeed; }
    public BoxId getBoxId() { return boxId; }
    public List<ItemId> getItemIds() { return ItemIds; }
    public Instant getStartTime() { return startTime; }
    public Instant getArrivalTime() { return arrivalTime; }
    public Instant getEstimatedArrivalTime() {return estimatedArrivalTime;}
    public boolean isDelivered() { return isDelivered; }
    public boolean isReturned() { return isReturned; }

}
