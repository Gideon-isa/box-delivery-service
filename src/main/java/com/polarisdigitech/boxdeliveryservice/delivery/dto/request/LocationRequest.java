package com.polarisdigitech.boxdeliveryservice.delivery.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public final record LocationRequest(

        @NotNull
        @Min(value = -90, message = "latitude can not be less than -90")
        @Max(value = 90, message = "latitude can not be greater than 90")
        Double latitude,

        @NotNull
        @Min(value = -180, message = "longitude can not be less than -180")
        @Max(value = 180, message = "longitude can not be greater than 180")
        Double longitude
) {
}
