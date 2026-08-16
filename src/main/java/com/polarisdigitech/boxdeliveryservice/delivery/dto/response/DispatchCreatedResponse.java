package com.polarisdigitech.boxdeliveryservice.delivery.dto.response;

import com.polarisdigitech.boxdeliveryservice.delivery.domain.Delivery;
import com.polarisdigitech.boxdeliveryservice.delivery.dto.DeliveryView;

import java.time.Instant;
import java.util.UUID;

public record DispatchCreatedResponse(UUID id, Instant estimatedDeliveryTime, boolean dispatchedSuccessful) {

    public static DispatchCreatedResponse from(DeliveryView delivery) {
        return new DispatchCreatedResponse(delivery.id(), delivery.estimatedArrivalTime(), true);
    }

}
