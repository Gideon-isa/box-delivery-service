package com.polarisdigitech.boxdeliveryservice.delivery.dto;

import com.polarisdigitech.boxdeliveryservice.delivery.domain.Delivery;
import com.polarisdigitech.boxdeliveryservice.item.domain.ItemId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final record  DeliveryView(
        UUID id,
        String remoteLocationName,
        double dispatchedLatitude,
        double dispatchedLongitude,
        double destinationLatitude,
        double destinationLongitude,
        double locationDistance,
        double boxSetSpeed,
        UUID boxId,
        List<ItemId>ItemIds,
        Instant startTime,
        Instant estimatedArrivalTime,
        Instant arrivalTime,
        boolean isDelivered,
        boolean isReturned) {

    public static DeliveryView from(Delivery delivery) {
        return new DeliveryView(
                delivery.getId(),
                delivery.getDestinationName(),
                delivery.getDispatchedLocationLatitude(),
                delivery.getDispatchedLocationLongitude(),
                delivery.getDestinationLatitude(),
                delivery.getDestinationLongitude(),
                delivery.getDestinationDistance(),
                delivery.getBoxSetSpeed(),
                delivery.getBoxId().getValue(),
                delivery.getItemIds(),
                delivery.getStartTime(),
                delivery.getEstimatedArrivalTime(),
                delivery.getArrivalTime(),
                delivery.isDelivered(),
                delivery.isReturned()
        );
    }
}
