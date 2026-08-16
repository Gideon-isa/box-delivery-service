package com.polarisdigitech.boxdeliveryservice.delivery.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EstimateFlightRequest(

        LocationRequest currentLocation,

        LocationRequest destinationLocation,

        @NotNull(message = "weightLimitGrams is required")
        @Min(value = 1, message = "drone-box minimum speed is can not be less than 1km/hr")
        @Max(value = 50, message = "drone-box maximum speed is 50km/hr")
        Double speed,

        @NotNull(message = "item weight can not be empty")
        @Min(value = 0, message = "total item weight can not be less than 0")
        @Max(value = 500, message = "maximum item's weight is 500 grams")
        Double itemTotalWeightInGrams
) { }
