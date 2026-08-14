package com.polarisdigitech.boxdeliveryservice.domain.Delivery;

import com.polarisdigitech.boxdeliveryservice.domain.box.BoxConstants;
import com.polarisdigitech.boxdeliveryservice.domain.box.BoxId;
import com.polarisdigitech.boxdeliveryservice.domain.item.Item;
import com.polarisdigitech.boxdeliveryservice.domain.shared.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class Delivery extends AggregateRoot<UUID> {
    private double locationDistance;
    private double boxSetSpeed;
    private BoxId boxId;
    private List<Item> Items;
    private Instant startTime;
    private Instant arrivalTime;
    private Instant returnedTime;
    private boolean isDelivered;
    private boolean isReturned;

    private Delivery(UUID id,
                    UUID createdBy,
                    double locationDistance,
                    double boxSetSpeed,
                    BoxId boxId,
                    List<Item> itemsList,
                    Instant startTime,
                    Instant arrivalTime,
                    Instant returnedTime) {
        super(id, createdBy);
        this.locationDistance = locationDistance;
        this.boxSetSpeed = boxSetSpeed;
        this.boxId = boxId;
        this.Items = itemsList;
        this.startTime = startTime;
        this.arrivalTime = arrivalTime;
        this.returnedTime = returnedTime;
    }

    public static Result<Delivery, DomainError> create(
            UUID createdBy,
            double locationDistance,
            double boxSetSpeed,
            BoxId boxId,
            List<Item> itemList) {

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
                itemList,
                Instant.now(),
                null,
                null));

    }

    public void MarkedAsReturned() {
        returnedTime = Instant.now();
        isReturned = true;
    }

    public void MarkedAsDelivered() {
        returnedTime = Instant.now();
        isReturned = true;
    }

    @Override
    public UUID getId() {
        return null;
    }
}
