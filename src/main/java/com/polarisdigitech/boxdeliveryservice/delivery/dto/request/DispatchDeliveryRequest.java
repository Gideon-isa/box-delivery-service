package com.polarisdigitech.boxdeliveryservice.delivery.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record DispatchDeliveryRequest(

        @NotNull(message = "box can not be null")
        UUID boxId,

        @NotNull
        @Min(value = 1, message = "drone-box minimum speed is can not be less than 1km/hr")
        @Max(value = 50, message = "drone-box maximum speed is 50km/hr")
        Double setSpeed,

        @NotNull
        @Size(
                min = 1,
                max = 50,
                message = "Remote location name must be between 1 and 50 characters"
        )
        String remoteLocationName,

        LocationRequest currentLocation,

        LocationRequest destinationLocation
) {

}
